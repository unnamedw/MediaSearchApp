package com.doach.mediasearchapp.android.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.model.MediaWithFavorite
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.domain.usecase.GetMediaFlowByQueryUseCase
import com.doach.mediasearchapp.android.presentation.MediaItemUiState
import com.doach.mediasearchapp.android.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMediaFlowByQueryUseCase: GetMediaFlowByQueryUseCase,
    private val mediaRepository: MediaRepository
): ViewModel() {

    private val _eventShowToast: SingleLiveEvent<String> = SingleLiveEvent()
    val eventShowToast: LiveData<String> = _eventShowToast

    private val _eventShowMediaDetail: SingleLiveEvent<Media> = SingleLiveEvent()
    val eventShowMediaDetail: LiveData<Media> = _eventShowMediaDetail

    private val queryFlow = MutableSharedFlow<String>()

    val mediaFlow: Flow<PagingData<MediaItemUiState>> = queryFlow.flatMapLatest {
        pagingDataFlow(it)
    }.map {
        it.map { mediaWithFavorite ->
            MediaItemUiState(
                media = mediaWithFavorite.media,
                onItemClick = ::onItemClick,
                onFavoriteClick = ::onFavoriteClick,
                isFavorite = mediaWithFavorite.isFavorite
            )
        }
    }.catch {
        _eventShowToast.value = it.message
//        emptyFlow<PagingData<MediaItemUiState>>()
    }.cachedIn(viewModelScope)

    private suspend fun pagingDataFlow(queryString: String): Flow<PagingData<MediaWithFavorite>> =
        getMediaFlowByQueryUseCase.invoke(queryString, viewModelScope)

    fun submitQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    private fun onItemClick(uiState: MediaItemUiState) {
        _eventShowMediaDetail.value = uiState.media
    }

    private fun onFavoriteClick(uiState: MediaItemUiState) {
        when {
            uiState.isFavorite -> mediaRepository.removeMedia(uiState.media)

            else -> mediaRepository.insertMedia(uiState.media)
        }
    }

    class Factory constructor(
        private val getMediaFlowByQueryUseCase: GetMediaFlowByQueryUseCase,
        private val mediaRepository: MediaRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                getMediaFlowByQueryUseCase, mediaRepository
            ) as T
        }
    }
}