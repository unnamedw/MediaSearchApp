package com.doach.mediasearchapp.android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.domain.model.Media
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONArray

class AppPreferencesImpl(context: Context): AppPreferences {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        context.packageName + context.getString(R.string.preferences_name),
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    private val _favoriteMediaFlow = MutableStateFlow(getAllFavoriteMedia())
    override fun getAllFavoriteMediaFlow() = _favoriteMediaFlow.asStateFlow()

//    override fun insertFavoriteMedia(vararg media: Media) {
//        val updatedMedia = getAllFavoriteMedia()
//            .toMutableSet().apply {
//                addAll(media)
//            }
//
//        prefs.edit {
//            putString(KEY_MEDIA, toJson(updatedMedia))
//            commit()
//        }
//    }

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
        prefs.edit { putString(KEY_MEDIA, toJson(updatedMedia)).commit() }
        _favoriteMediaFlow.value = updatedMedia.toList()
    }

    override fun removeFavoriteMedia(vararg media: Media) {
        val toBeRemovedUrl = media.iterator().asSequence().map { it.url }.toSet()
        getAllFavoriteMedia().filterNot { toBeRemovedUrl.contains(it.url) }.also { updatedList ->
            prefs.edit { putString(KEY_MEDIA, toJson(updatedList)).commit() }
            _favoriteMediaFlow.value = updatedList
        }
    }

    override fun removeAllFavoriteMedia() {
        prefs.edit { putString(KEY_MEDIA, JsonArray().asString).commit() }
        _favoriteMediaFlow.value = emptyList()
    }

//    override fun getAllFavoriteMediaFlow(): Flow<List<Media>> = callbackFlow {
//        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//            if (key == KEY_MEDIA) {
//                trySend(getAllFavoriteMedia()).isSuccess // trySend()는 값을 보내고 성공 여부를 반환
//            }
//        }
//
//        prefs.registerOnSharedPreferenceChangeListener(listener)
//
//        // Flow의 취소 시점을 감지하여 리스너를 해제
//        awaitClose {
//            prefs.unregisterOnSharedPreferenceChangeListener(listener)
//        }
//    }


//    suspend fun getAllFavoriteMediaFlow(): Flow<List<Media>> = suspendCoroutine { continuation ->
//        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
//            if (key == KEY_MEDIA) {
//                val json = prefs.getString(key, null)
//                val list = if (json == null) emptyList() else fromJson<List<Media>>(json)
//                continuation.resume(list)
//            }
//        }
//
//        prefs.registerOnSharedPreferenceChangeListener(listener)
//        continuation.invokeOnCancellation {
//            prefs.unregisterOnSharedPreferenceChangeListener(listener)
//        }
//    }

//    override fun removeFavoriteMedia(vararg media: Media) {
//        val toBeRemovedUrl = media.iterator()
//            .asSequence()
//            .map { it.url }
//            .toSet()
//
//        getAllFavoriteMedia()
//            .filterNot { toBeRemovedUrl.contains(it.url) }
//            .also { updatedList ->
//                prefs.edit {
//                    putString(KEY_MEDIA, toJson(updatedList))
//                    commit()
//                }
//            }
//    }
//
//    override fun removeAllFavoriteMedia() {
//        prefs.edit {
//            putString(KEY_MEDIA, toJson(emptyList<Media>()))
//            commit()
//        }
//    }

    private fun toJson(data: Any): String {
        return gson.toJson(data) as String
    }

    private fun <T> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>(){}.type)
    }

    companion object {
        private const val KEY_MEDIA = "KEY_MEDIA"
    }

}