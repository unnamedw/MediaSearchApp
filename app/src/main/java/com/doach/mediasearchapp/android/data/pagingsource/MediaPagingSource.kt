package com.doach.mediasearchapp.android.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.domain.model.Media
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MediaPagingSource(
    private val api: ApiService,
    private val query: String
): PagingSource<Int, Media>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> = coroutineScope {
        Timber.d("paging test >> key: ${params.key}, loadSize: ${params.loadSize}")
        return@coroutineScope try {
            val nextPageNumber = params.key ?: startPage
            val videoResponseAsync = async(this.coroutineContext) {
                try {
                    api.getVideoList(
                        query = query,
                        sort = "recency",
                        page = nextPageNumber
                    )
                } catch (e: Exception) {
                    null
                }
            }
            val imageResponseAsync = async(this.coroutineContext) {
                try {
                    api.getImageList(
                        query = query,
                        sort = "recency",
                        page = nextPageNumber
                    )
                } catch (e: Exception) {
                    null
                }
            }

            val videoResponse = videoResponseAsync.await()
            val imageResponse = imageResponseAsync.await()

            val nextKey = if (imageResponse?.meta?.isEnd != false && videoResponse?.meta?.isEnd != false) {
                null
            } else {
                nextPageNumber + 1
            }

            Timber.d("nextKey >> $nextKey")

            val videoList = videoResponse?.documents?.map { it.toDomainModel() } ?: emptyList()
            val imageList = imageResponse?.documents?.map { it.toDomainModel() } ?: emptyList()

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
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val startPage = 1
    }

}