package com.doach.mediasearchapp.android.data.remote

import com.doach.mediasearchapp.android.data.remote.dto.VideoResponse
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService

class VideoRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getVideoList(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
    ): List<VideoResponse.VideoApiModel> = apiService.getVideoList(query, sort, page, size).documents

}