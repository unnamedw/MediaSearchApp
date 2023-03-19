package com.doach.mediasearchapp.android.data.remote

import com.doach.mediasearchapp.android.data.remote.dto.ImageResponse
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService

class ImageRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getImageList(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
    ): List<ImageResponse.ImageApiModel> = apiService.getImageList(query, sort, page, size).documents

}