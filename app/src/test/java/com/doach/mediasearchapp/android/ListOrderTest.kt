package com.doach.mediasearchapp.android

import com.doach.mediasearchapp.android.data.remote.dto.ImageResponse
import com.doach.mediasearchapp.android.data.remote.dto.VideoResponse
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.mock.mockImagePageResponse
import com.doach.mediasearchapp.android.mock.mockVideoPageResponse
import com.doach.mediasearchapp.android.presentation.toFormattedTime
import com.google.gson.Gson

fun main() {
    val gson = Gson()
    val videoList = gson.fromJson(mockVideoPageResponse, VideoResponse::class.java)
        .documents
        .map { it.toDomainModel() }
    val imageList = gson.fromJson(mockImagePageResponse, ImageResponse::class.java)
        .documents
        .map { it.toDomainModel() }

    videoList.plus(imageList)
        .sortedByDescending { it.timestamp }
        .map { it.timestamp.toFormattedTime("yyyy.MM.dd HH:mm:ss") }
        .forEach { println(it) }
}