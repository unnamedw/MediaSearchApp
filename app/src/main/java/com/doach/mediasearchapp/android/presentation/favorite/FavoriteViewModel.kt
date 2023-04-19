package com.doach.mediasearchapp.android.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.presentation.MediaItemUiState
import com.doach.mediasearchapp.android.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.flow.map
import timber.log.Timber

class FavoriteViewModel(
    private val mediaRepository: MediaRepository
): ViewModel() {

    private val _eventShowMediaDetail: SingleLiveEvent<Media> = SingleLiveEvent()
    val eventShowMediaDetail: LiveData<Media> = _eventShowMediaDetail

    init {
        Timber.d("init")
    }

    /**
     * 즐겨찾기를 해제하여도 리스트에서 남겨두기 위한 처리
     * **/
    private val initialFavoriteList = mediaRepository.getFavoriteMedia()
    val favoriteListFlow = mediaRepository.getFavoriteMediaFlow().map { updatedList ->
        initialFavoriteList.map { media ->
            val isMediaExistInUpdatedFavoriteList = updatedList.find { it.url == media.url } != null
            MediaItemUiState(
                media = media,
                onItemClick = ::onItemClick,
                onFavoriteClick = ::onFavoriteClick,
                isFavorite = isMediaExistInUpdatedFavoriteList
            )
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

    class Factory(
        private val mediaRepository: MediaRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(mediaRepository) as T
        }
    }
}