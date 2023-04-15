package com.doach.mediasearchapp.android.utils

import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.model.Video
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MediaDeserializer: JsonDeserializer<Media> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Media {
        return try {
            Gson().fromJson(json, Image::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Gson().fromJson(json, Video::class.java)
        }
    }
}