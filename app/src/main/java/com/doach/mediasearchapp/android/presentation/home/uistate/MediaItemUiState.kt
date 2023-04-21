package com.doach.mediasearchapp.android.presentation.home.uistate

import com.doach.mediasearchapp.android.domain.model.Media

data class MediaItemUiState(
    val media: Media,
    val onItemClick: (MediaItemUiState) -> Unit,
    val onFavoriteClick: (MediaItemUiState) -> Unit,
    val isFavorite: Boolean = false
)