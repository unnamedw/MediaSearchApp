package com.doach.mediasearchapp.android.domain.model

data class Video(
    val title: String,
    override val thumbnailUrl: String,
    override val url: String,
    override val timestamp: Long,
    val playTimeSeconds: Int
): Media()