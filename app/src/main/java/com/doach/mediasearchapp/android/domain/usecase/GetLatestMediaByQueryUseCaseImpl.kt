package com.doach.mediasearchapp.android.domain.usecase

import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.repository.ImageRepository
import com.doach.mediasearchapp.android.domain.repository.VideoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetLatestMediaByQueryUseCaseImpl(
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
): GetLatestMediaByQueryUseCase {

    override suspend fun invoke(query: String): List<Media> = coroutineScope {
        val imageListDeferred = async { imageRepository.getImageList(query = query, sort = "recency") }
        val videoListDeferred = async { videoRepository.getVideoList(query = query, sort = "recency") }

        val imageList = imageListDeferred.await()
        val videoList = videoListDeferred.await()

        // 최신 순으로 가져온 목록을 조합하여 다시 한 번 내림차순으로 정렬
        imageList.plus(videoList).sortedByDescending { it.timestamp }
    }
}