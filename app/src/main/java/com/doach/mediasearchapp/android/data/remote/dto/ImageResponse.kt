package com.doach.mediasearchapp.android.data.remote.dto

import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.utils.Iso8601Util
import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("documents") val documents: List<ImageApiModel>,
    @SerializedName("meta") val meta: Meta
) {
    data class ImageApiModel(
        @SerializedName("collection") val collection: String,
        @SerializedName("datetime") val datetime: String,
        @SerializedName("display_sitename") val displaySiteName: String,
        @SerializedName("doc_url") val docUrl: String,
        @SerializedName("height") val height: Int,
        @SerializedName("image_url") val imageUrl: String,
        @SerializedName("thumbnail_url") val thumbnailUrl: String,
        @SerializedName("width") val width: Int
    )
    data class Meta(
        @SerializedName("is_end") val isEnd: Boolean,
        @SerializedName("pageable_count") val pageableCount: Int,
        @SerializedName("total_count") val totalCount: Int
    )
}

fun ImageResponse.ImageApiModel.toDomainModel(): Image {
    return Image(
        title = collection,
        thumbnailUrl = thumbnailUrl,
        url = imageUrl,
        timestamp = Iso8601Util.toMillis(datetime),
        width = width,
        height = height,
    )
}