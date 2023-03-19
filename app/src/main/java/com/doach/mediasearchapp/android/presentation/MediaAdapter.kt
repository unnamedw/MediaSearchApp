package com.doach.mediasearchapp.android.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.doach.mediasearchapp.android.databinding.MediaViewholderBinding

class MediaAdapter: PagingDataAdapter<MediaItemUiState, MediaViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            MediaViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItemUiState>() {
            override fun areItemsTheSame(oldItem: MediaItemUiState, newItem: MediaItemUiState): Boolean {
                return oldItem.media.url == newItem.media.url
            }

            override fun areContentsTheSame(oldItem: MediaItemUiState, newItem: MediaItemUiState): Boolean {
                return oldItem == newItem
            }

        }
    }
}