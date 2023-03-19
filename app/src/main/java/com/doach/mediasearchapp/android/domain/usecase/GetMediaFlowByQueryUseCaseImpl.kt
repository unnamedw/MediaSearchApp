package com.doach.mediasearchapp.android.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.doach.mediasearchapp.android.domain.model.MediaWithFavorite
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMediaFlowByQueryUseCaseImpl(
    private val mediaRepository: MediaRepository
): GetMediaFlowByQueryUseCase {

    override suspend fun invoke(query: String): Flow<PagingData<MediaWithFavorite>> {
//        val favoriteList = mediaRepository.getFavoriteMediaFlow().map {
//            it.map { media ->
//                media.url
//            }
//        }

        return mediaRepository.getMediaFlow(query).map {
            it.map { media ->
                MediaWithFavorite(
                    media = media,
                    isFavorite = false
                )
            }
        }
    }

}