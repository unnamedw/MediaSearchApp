package com.doach.mediasearchapp.android.data.local

import com.doach.mediasearchapp.android.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchDao {

    fun insertSearchHistory(vararg searchHistory: SearchHistory)

    fun getSearchHistory(): List<SearchHistory>

    fun getSearchHistoryFlow(): Flow<List<SearchHistory>>

    fun removeSearchHistory(vararg searchHistory: SearchHistory)

    fun clearHistory()

}