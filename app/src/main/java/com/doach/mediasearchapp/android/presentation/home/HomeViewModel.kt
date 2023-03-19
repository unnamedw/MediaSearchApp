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

    val mediaFlow = queryFlow.flatMapLatest {
        pagingDataFlow(it)
    }.map {
        it.map { mediaWithFavorite ->
            MediaItemUiState(
                media = mediaWithFavorite.media,
                onClick = { clickMediaItem(mediaWithFavorite.media) },
                onFavoriteClick = { state -> clickFavorite(state) },
                isFavorite = mediaWithFavorite.isFavorite
            )
        }
    }.catch {
        _eventShowToast.value = it.message
    }.cachedIn(viewModelScope)

    private suspend fun pagingDataFlow(queryString: String): Flow<PagingData<MediaWithFavorite>> =
        getMediaFlowByQueryUseCase(queryString)

    fun submitQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    private fun clickMediaItem(media: Media) {
        _eventShowMediaDetail.value = media
    }

    private fun clickFavorite(uiState: MediaItemUiState) {
        when {
            uiState.isFavorite -> {
//                mediaRepository.insertMedia()
            }
            else -> {
//                mediaRepository.removeMedia()
            }
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