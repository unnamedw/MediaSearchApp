package com.doach.mediasearchapp.android.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doach.mediasearchapp.android.data.pagingsource.MediaPagingSource
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.data.local.AppPreferences
import com.doach.mediasearchapp.android.domain.model.Media
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class MediaRepositoryImpl(
    private val api: ApiService,
    private val appPreferences: AppPreferences,
    private val ioDispatcher: CoroutineDispatcher
): MediaRepository {

    override fun getMediaFlow(query: String): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MediaPagingSource(api, query)
            }
        ).flow
            .flowOn(ioDispatcher)
    }

    override fun getFavoriteMediaFlow(): Flow<List<Media>> {
        return appPreferences.getAllFavoriteMediaFlow()
    }

    override fun insertMedia(vararg media: Media) {
        appPreferences.insertFavoriteMedia(*media)
    }

    override fun removeMedia(vararg media: Media) {
        appPreferences.removeFavoriteMedia(*media)
    }

    companion object {
        const val PAGE_SIZE = 15
    }

}