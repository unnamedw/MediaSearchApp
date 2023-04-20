package com.doach.mediasearchapp.android.data.remote.retrofit

import com.doach.mediasearchapp.android.data.remote.dto.ImageResponse
import com.doach.mediasearchapp.android.data.remote.dto.VideoResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/search/vclip")
    suspend fun getVideoList(
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): VideoResponse

    @GET("/v2/search/image")
    suspend fun getImageList(
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): ImageResponse
}