package com.doach.mediasearchapp.android.data.remote.dto

import com.doach.mediasearchapp.android.domain.model.Video
import com.doach.mediasearchapp.android.utils.Iso8601Util
import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("documents") val documents: List<VideoApiModel>,
    @SerializedName("meta") val meta: Meta
) {
    data class Meta(
        @SerializedName("is_end") val isEnd: Boolean,
        @SerializedName("pageable_count") val pageableCount: Int,
        @SerializedName("total_count") val totalCount: Int
    )

    data class VideoApiModel(
        @SerializedName("author") val author: String,
        @SerializedName("datetime") val datetime: String,
        @SerializedName("play_time") val playTime: Int,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("title") val title: String,
        @SerializedName("url") val url: String
    )

}

fun VideoResponse.VideoApiModel.toDomainModel(): Video {
    return Video(
        title = title,
        thumbnailUrl = thumbnail,
        url = url,
        timestamp = Iso8601Util.toMillis(datetime),
        playTimeSeconds = playTime
    )
}