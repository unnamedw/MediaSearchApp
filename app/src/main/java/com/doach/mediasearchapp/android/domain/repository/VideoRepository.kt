package com.doach.mediasearchapp.android.domain.repository

import androidx.paging.PagingData
import com.doach.mediasearchapp.android.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun getVideoList(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
    ): List<Video>

    fun getVideoStream(query: String): Flow<PagingData<Video>>

}