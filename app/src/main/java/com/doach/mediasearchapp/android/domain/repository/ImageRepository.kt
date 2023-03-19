package com.doach.mediasearchapp.android.domain.repository

import androidx.paging.PagingData
import com.doach.mediasearchapp.android.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImageList(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
    ): List<Image>

    fun getImageStream(query: String): Flow<PagingData<Image>>

}