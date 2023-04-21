package com.doach.mediasearchapp.android.domain.repository

import com.doach.mediasearchapp.android.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun getSearchHistoryFlow(): Flow<List<SearchHistory>>

    fun getSearchHistory(): List<SearchHistory>

    fun insertSearchHistory(vararg searchHistory: SearchHistory)

    fun removeSearchHistory(vararg searchHistory: SearchHistory)

    fun clearHistory()

}