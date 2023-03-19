package com.doach.mediasearchapp.android.domain.model

sealed class Media {
    abstract val url: String
    abstract val thumbnailUrl: String
    abstract val timestamp: Long
}

data class MediaWithFavorite(
    val media: Media,
    val isFavorite: Boolean
)
