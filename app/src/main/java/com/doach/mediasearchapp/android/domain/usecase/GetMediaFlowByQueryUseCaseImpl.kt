package com.doach.mediasearchapp.android.domain.usecase

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import androidx.paging.cachedIn
import androidx.paging.map
import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.model.MediaWithFavorite
import com.doach.mediasearchapp.android.domain.repository.ImageRepository
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.domain.repository.VideoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class GetMediaFlowByQueryUseCaseImpl(
    private val mediaRepository: MediaRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
): GetMediaFlowByQueryUseCase {

    override suspend fun invoke(query: String, scope: CoroutineScope): Flow<PagingData<MediaWithFavorite>> {
        return combine(
            merge(
                imageRepository.getImageStream(query),
                videoRepository.getVideoStream(query)
            ),
            mediaRepository.getFavoriteMediaFlow()
        ) { items, favorites ->
            items.map { media ->
                MediaWithFavorite(
                    media = media,
                    isFavorite = favorites.map { it.url }.contains(media.url)
                )
            }
        }
    }
}