package com.doach.mediasearchapp.android.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.doach.mediasearchapp.android.domain.model.MediaWithFavorite
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMediaFlowByQueryUseCaseImpl(
    private val mediaRepository: MediaRepository
): GetMediaFlowByQueryUseCase {

    override suspend fun invoke(query: String, scope: CoroutineScope): Flow<PagingData<MediaWithFavorite>> {
        return combine(
            mediaRepository.getMediaFlow(query).cachedIn(scope),
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