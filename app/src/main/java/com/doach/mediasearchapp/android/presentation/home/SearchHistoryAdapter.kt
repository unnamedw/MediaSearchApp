package com.doach.mediasearchapp.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doach.mediasearchapp.android.databinding.SearchHistoryViewholderBinding
import com.doach.mediasearchapp.android.presentation.home.uistate.SearchHistoryItemUiState

class SearchHistoryAdapter:
    ListAdapter<SearchHistoryItemUiState, SearchHistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder(
            SearchHistoryViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryItemUiState>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryItemUiState,
                newItem: SearchHistoryItemUiState
            ): Boolean {
                return oldItem.item.query == newItem.item.query
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryItemUiState,
                newItem: SearchHistoryItemUiState
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}

class SearchHistoryViewHolder(
    private val binding: SearchHistoryViewholderBinding
): RecyclerView.ViewHolder(binding.root) {

    private var uiState: SearchHistoryItemUiState? = null

    init {
        binding.layoutRemoveIcon.setOnClickListener {
            uiState?.let { state -> state.onRemoveClick(state) }
        }

        binding.root.setOnClickListener {
            uiState?.let { state -> state.onClick(state) }
        }
    }

    fun bind(uiState: SearchHistoryItemUiState) {
        this.uiState = uiState
        binding.tvSearchHistory.text = uiState.item.query
    }

}