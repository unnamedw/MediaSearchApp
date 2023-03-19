package com.doach.mediasearchapp.android.domain.model

data class Image(
    val title: String,
    override val thumbnailUrl: String,
    override val url: String,
    override val timestamp: Long,
    val width: Int,
    val height: Int
): Media()