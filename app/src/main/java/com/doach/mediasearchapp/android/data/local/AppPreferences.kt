package com.doach.mediasearchapp.android.data.local

import com.doach.mediasearchapp.android.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    fun insertFavoriteMedia(vararg media: Media)

    fun getAllFavoriteMedia(): List<Media>

    fun getAllFavoriteMediaFlow(): Flow<List<Media>>

    fun removeFavoriteMedia(vararg media: Media)

    fun removeAllFavoriteMedia()

}