package com.doach.mediasearchapp.android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.model.SearchHistory
import com.doach.mediasearchapp.android.utils.MediaDeserializer
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

class AppPreferencesImpl(context: Context): MediaDao, SearchDao {

    // TODO("Dao 를 Generic 으로 모듈화하여 각각의 모델에 대해 분리하는 방향으로 개선")

    private val prefs: SharedPreferences = context.getSharedPreferences(
        context.packageName + context.getString(R.string.preferences_name),
        Context.MODE_PRIVATE
    )

    private val gson = GsonBuilder()
        .registerTypeAdapter(Media::class.java, MediaDeserializer())
        .create()


    /**
     * Media
     * **/
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
        val updatedList = getAllFavoriteMedia().toMutableSet()
        val newList = media
            .filterNot { toBeAdded ->
                val existingUrl = updatedList.map { it.url }
                existingUrl.contains(toBeAdded.url)
            }

        updatedList.addAll(newList)

        prefs.edit { putString(KEY_MEDIA, gson.toJson(updatedList)).commit() }
        _favoriteMediaFlow.value = updatedList.toList()
    }

    override fun removeFavoriteMedia(vararg media: Media) {
        val toBeRemovedUrl = media.iterator().asSequence().map { it.url }.toSet()
        getAllFavoriteMedia().filterNot { toBeRemovedUrl.contains(it.url) }.also { updatedList ->
            prefs.edit { putString(KEY_MEDIA, gson.toJson(updatedList)).commit() }
            _favoriteMediaFlow.value = updatedList
        }
    }

    override fun clearFavoriteMedia() {
        prefs.edit { putString(KEY_MEDIA, JSONArray(arrayOf<Media>()).toString()).commit() }
        _favoriteMediaFlow.value = emptyList()
    }


    /**
     * Search History
     * **/
    private val _searchHistoryFlow = MutableStateFlow(getSearchHistory())

    override fun getSearchHistoryFlow(): Flow<List<SearchHistory>> = _searchHistoryFlow.asStateFlow()

    override fun getSearchHistory(): List<SearchHistory> {
        return prefs.getString(KEY_SEARCH_HISTORY, JSONArray(arrayOf<SearchHistory>()).toString())?.let {

            val arr = JSONArray(it)
            List(arr.length()) { index ->
                val element = arr.getJSONObject(index)
                gson.fromJson(element.toString(), SearchHistory::class.java)
            }

        } ?: emptyList()
    }

    override fun insertSearchHistory(vararg searchHistory: SearchHistory) {
        val updatedHistory = getSearchHistory().toMutableSet()
        val newHistory = searchHistory
            .filterNot { toBeAdded ->
                val existingQuery = updatedHistory.map { history -> history.query }
                existingQuery.contains(toBeAdded.query)
            }

        updatedHistory.addAll(newHistory)

        prefs.edit { putString(KEY_SEARCH_HISTORY, gson.toJson(updatedHistory)).commit() }
        _searchHistoryFlow.value = updatedHistory.toList()
    }

    override fun removeSearchHistory(vararg searchHistory: SearchHistory) {
        val toBeRemovedQuery = searchHistory.iterator().asSequence().map { it.query }.toSet()
        getSearchHistory().filterNot { toBeRemovedQuery.contains(it.query) }.also { updatedList ->
            prefs.edit { putString(KEY_SEARCH_HISTORY, gson.toJson(updatedList)).commit() }
            _searchHistoryFlow.value = updatedList
        }
    }

    override fun clearHistory() {
        prefs.edit { putString(KEY_SEARCH_HISTORY, JSONArray(arrayOf<SearchHistory>()).toString()).commit() }
        _searchHistoryFlow.value = emptyList()
    }

    companion object {
        private const val KEY_MEDIA = "KEY_MEDIA"
        private const val KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY"
    }

}

