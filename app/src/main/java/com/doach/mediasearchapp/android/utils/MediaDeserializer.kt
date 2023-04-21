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

    private val gson by lazy { Gson() }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Media? {

        if (json == null) {
            return null
        }

        // TODO(
        //  현재는 재생시간 필드로 구분하고 있으나 Media 내 필드 변경에 안전한 방법이 아니므로 더 좋은 방법을 고려해야 한다.
        //  다만, 도메인 모델은 변경이 잦지 않은 클래스이므로 우선 해당 필드로 구분자를 지정함.
        //  )
        return if (json.asJsonObject.has("playTimeSeconds")) {
            gson.fromJson(json, Video::class.java)
        } else {
            gson.fromJson(json, Image::class.java)
        }
    }
}