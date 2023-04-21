package com.doach.mediasearchapp.android.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.presentation.home.uistate.MediaItemUiState
import com.doach.mediasearchapp.android.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoriteViewModel(
    private val mediaRepository: MediaRepository
): ViewModel() {

    private val _eventShowMediaDetail: SingleLiveEvent<Media> = SingleLiveEvent()
    val eventShowMediaDetail: LiveData<Media> = _eventShowMediaDetail

    init {
        Timber.d("init")
    }

    private val sortTypeFlow = MutableStateFlow(SortType.DEFAULT)

    /**
     * 즐겨찾기를 해제하여도 리스트에서 남겨두기 위한 처리
     * **/
    private val initialFavoriteList = mediaRepository.getFavoriteMedia()
    val favoriteListFlow = combine(
        mediaRepository.getFavoriteMediaFlow(),
        sortTypeFlow
    ) { updatedList, sortType ->

        val list = initialFavoriteList.map { media ->

            // 좋아요 목록이 업데이트되면 체크표시에 반영
            val isMediaExistInUpdatedFavoriteList = updatedList.find { it.url == media.url } != null

            MediaItemUiState(
                media = media,
                onItemClick = ::onItemClick,
                onFavoriteClick = ::onFavoriteClick,
                isFavorite = isMediaExistInUpdatedFavoriteList
            )
        }

        return@combine when (sortType) {
            SortType.DEFAULT -> list
            SortType.RECENT -> list.sortedByDescending { item -> item.media.timestamp }
        }
    }

    private fun onFavoriteClick(uiState: MediaItemUiState) {
        when {
            uiState.isFavorite -> mediaRepository.removeMedia(uiState.media)
            else -> mediaRepository.insertMedia(uiState.media)
        }
    }

    private fun onItemClick(uiState: MediaItemUiState) {
        _eventShowMediaDetail.value = uiState.media
    }

    fun changeSortType(sortType: SortType) {
        viewModelScope.launch {
            sortTypeFlow.emit(sortType)
        }
    }

    class Factory(
        private val mediaRepository: MediaRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(mediaRepository) as T
        }
    }
}

enum class SortType {
    DEFAULT,
    RECENT
}