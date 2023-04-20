package com.doach.mediasearchapp.android.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.domain.model.Media
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MediaPagingSource(
    private val api: ApiService,
    private val query: String
): PagingSource<Int, Media>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> = supervisorScope {
        Timber.d("paging test >> key: ${params.key}, loadSize: ${params.loadSize}")
        return@supervisorScope try {
            val nextPageNumber = params.key ?: startPage
            val videoResponseAsync = async {
                api.getVideoList(
                    query = query,
                    sort = "recency",
                    page = nextPageNumber
                )
            }
            val imageResponseAsync = async {
                api.getImageList(
                    query = query,
                    sort = "recency",
                    page = nextPageNumber
                )
            }

            val videoResponse = videoResponseAsync.await()
            val imageResponse = imageResponseAsync.await()

            val nextKey = if (imageResponse.meta.isEnd && videoResponse.meta.isEnd) {
                null
            } else {
                nextPageNumber + 1
            }

            Timber.d("nextKey >> $nextKey")

            val videoList = videoResponse.documents.map { it.toDomainModel() }
            val imageList = imageResponse.documents.map { it.toDomainModel() }

            val mediaList = videoList.plus(imageList).sortedByDescending { it.timestamp }

            LoadResult.Page(
                data = mediaList,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            if (e.code() == 400) {
                LoadResult.Error(Exception("페이지가 더 이상 존재하지 않습니다"))
            } else {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val startPage = 1
    }

}