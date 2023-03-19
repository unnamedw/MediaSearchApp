package com.doach.mediasearchapp.android.domain.repository

import androidx.paging.PagingData
import com.doach.mediasearchapp.android.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getMediaFlow(query: String): Flow<PagingData<Media>>

    fun getFavoriteMediaFlow(): Flow<List<Media>>

    fun insertMedia(vararg media: Media)

    fun removeMedia(vararg media: Media)

}