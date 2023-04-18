package com.doach.mediasearchapp.android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.utils.MediaDeserializer
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

class AppPreferencesImpl(context: Context): AppPreferences {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        context.packageName + context.getString(R.string.preferences_name),
        Context.MODE_PRIVATE
    )

    private val gson = GsonBuilder()
        .registerTypeAdapter(Media::class.java, MediaDeserializer())
        .create()

    private val _favoriteMediaFlow = MutableStateFlow(getAllFavoriteMedia())
    override fun getAllFavoriteMediaFlow() = _favoriteMediaFlow.asStateFlow()

    override fun getAllFavoriteMedia(): List<Media> {
        return prefs.getString(KEY_MEDIA, JSONArray(arrayOf<Media>()).toString())?.let {
            val arr = JSONArray(it)
            List(arr.length()) { index ->
                val element = arr.getJSONObject(index)
                gson.fromJson(element.toString(), Media::class.java)
            }
        } ?: emptyList()
    }

    override fun insertFavoriteMedia(vararg media: Media) {
        val updatedMedia = getAllFavoriteMedia().toMutableSet().apply { addAll(media) }
        prefs.edit { putString(KEY_MEDIA, gson.toJson(updatedMedia)).commit() }
        _favoriteMediaFlow.value = updatedMedia.toList()
    }

    override fun removeFavoriteMedia(vararg media: Media) {
        val toBeRemovedUrl = media.iterator().asSequence().map { it.url }.toSet()
        getAllFavoriteMedia().filterNot { toBeRemovedUrl.contains(it.url) }.also { updatedList ->
            prefs.edit { putString(KEY_MEDIA, gson.toJson(updatedList)).commit() }
            _favoriteMediaFlow.value = updatedList
        }
    }

    override fun removeAllFavoriteMedia() {
        prefs.edit { putString(KEY_MEDIA, JsonArray().asString).commit() }
        _favoriteMediaFlow.value = emptyList()
    }

    companion object {
        private const val KEY_MEDIA = "KEY_MEDIA"
    }

}