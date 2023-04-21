package com.doach.mediasearchapp.android.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doach.mediasearchapp.android.data.local.MediaDao
import com.doach.mediasearchapp.android.data.pagingsource.VideoPagingSource
import com.doach.mediasearchapp.android.data.remote.VideoRemoteDataSource
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.domain.model.Video
import com.doach.mediasearchapp.android.domain.repository.VideoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VideoRepositoryImpl(
    private val api: ApiService,
    private val videoRemoteDataSource: VideoRemoteDataSource,
    private val mediaDao: MediaDao,
    private val ioDispatcher: CoroutineDispatcher
): VideoRepository {
    override suspend fun getVideoList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?
    ): List<Video> = withContext(ioDispatcher) {
        videoRemoteDataSource.getVideoList(query, sort, page, size).map { it.toDomainModel() }
    }

    override fun getVideoStream(query: String): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VideoPagingSource(api, query)
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}