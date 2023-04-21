package com.doach.mediasearchapp.android.presentation.home.uistate

import com.doach.mediasearchapp.android.domain.model.SearchHistory

data class SearchHistoryItemUiState(
    val item: SearchHistory,
    val onClick: (SearchHistoryItemUiState) -> Unit,
    val onRemoveClick: (SearchHistoryItemUiState) -> Unit
)