package com.doach.mediasearchapp.android.presentation

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.databinding.MediaViewholderBinding
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.model.Video

class MediaViewHolder(
    private val binding: MediaViewholderBinding
): RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context
    private var uiState: MediaItemUiState? = null
    init {
        binding.frameThumbnail.setOnClickListener {
            uiState?.let {
                it.onItemClick.invoke(it)
            }
        }

        binding.ivFavoriteIcon.setOnClickListener {
            uiState?.let {
                it.onFavoriteClick.invoke(it)
            }
        }
    }
    fun bind(uiState: MediaItemUiState) {
        this.uiState = uiState

        when {
            uiState.isFavorite -> binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_active)
            else -> binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_inactive)
        }

        when(uiState.media) {
            is Video -> {
                binding.cvPlaytime.visibility = View.VISIBLE
                binding.tvPlaytime.text = uiState.media.playTimeSeconds.toLong().formatDuration()
            }
            else -> {
                binding.cvPlaytime.visibility = View.GONE
            }
        }

        binding.tvDate.text = uiState.media.timestamp.toFormattedTime("yyyy-MM-dd HH:mm:ss")

        Glide.with(context)
            .load(uiState.media.thumbnailUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_error)
            .centerCrop()
            .into(binding.ivThumbnail)
    }
}
data class MediaItemUiState(
    val media: Media,
    val onItemClick: (MediaItemUiState) -> Unit,
    val onFavoriteClick: (MediaItemUiState) -> Unit,
    val isFavorite: Boolean = false
)