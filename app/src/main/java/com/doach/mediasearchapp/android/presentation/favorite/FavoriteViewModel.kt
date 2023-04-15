package com.doach.mediasearchapp.android.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.presentation.MediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class FavoriteViewModel(
    private val mediaRepository: MediaRepository
): ViewModel() {

    init {
        Timber.d("init")
    }

    val favoriteList: Flow<List<MediaItemUiState>> = mediaRepository.getFavoriteMediaFlow().map {
        it.map { media ->
            MediaItemUiState(
                media = media,
                onClick = {  },
                onFavoriteClick = { state -> clickFavorite(state) },
                isFavorite = true
            )
        }
    }

    private fun clickFavorite(uiState: MediaItemUiState) {
        when {
            uiState.isFavorite -> mediaRepository.removeMedia(uiState.media)

            else -> mediaRepository.insertMedia(uiState.media)
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