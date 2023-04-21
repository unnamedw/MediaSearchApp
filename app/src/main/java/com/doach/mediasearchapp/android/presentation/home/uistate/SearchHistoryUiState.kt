package com.doach.mediasearchapp.android.presentation.home.uistate

data class SearchHistoryUiState(
    val historyList: List<SearchHistoryItemUiState> = emptyList(),
    val isVisible: Boolean = false
)