package com.doach.mediasearchapp.android.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doach.mediasearchapp.android.data.local.AppPreferencesImpl
import com.doach.mediasearchapp.android.data.local.MediaDao
import com.doach.mediasearchapp.android.data.pagingsource.ImagePagingSource
import com.doach.mediasearchapp.android.data.remote.ImageRemoteDataSource
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.domain.model.Image
import com.doach.mediasearchapp.android.domain.repository.ImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImageRepositoryImpl(
    private val api: ApiService,
    private val imageRemoteDataSource: ImageRemoteDataSource,
    private val mediaDao: MediaDao,
    private val ioDispatcher: CoroutineDispatcher
): ImageRepository {
    override suspend fun getImageList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?
    ): List<Image> = withContext(ioDispatcher) {
        imageRemoteDataSource.getImageList(query, sort, page, size).map { it.toDomainModel() }
    }

    override fun getImageStream(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagePagingSource(api, query)
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 80
    }

}