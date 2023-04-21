package com.doach.mediasearchapp.android.data

import com.doach.mediasearchapp.android.data.local.SearchDao
import com.doach.mediasearchapp.android.domain.model.SearchHistory
import com.doach.mediasearchapp.android.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class SearchHistoryRepositoryImpl(
    private val searchDao: SearchDao
): SearchHistoryRepository {

    override fun getSearchHistoryFlow(): Flow<List<SearchHistory>> {
        return searchDao.getSearchHistoryFlow()
    }

    override fun getSearchHistory(): List<SearchHistory> {
        return searchDao.getSearchHistory()
    }

    override fun insertSearchHistory(vararg searchHistory: SearchHistory) {
        searchDao.insertSearchHistory(*searchHistory)
    }

    override fun removeSearchHistory(vararg searchHistory: SearchHistory) {
        searchDao.removeSearchHistory(*searchHistory)
    }

    override fun clearHistory() {
        searchDao.clearHistory()
    }
}