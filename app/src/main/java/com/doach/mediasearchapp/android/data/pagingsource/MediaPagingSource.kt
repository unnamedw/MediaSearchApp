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
import java.io.InvalidClassException

class MediaPagingSource(
    private val api: ApiService,
    private val query: String
): PagingSource<Int, Media>() {

    private var isImageChunkEnd = false
    private var isVideoChunkEnd = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> = supervisorScope {
        Timber.d("paging test >> key: ${params.key}, loadSize: ${params.loadSize}")

        return@supervisorScope try {
            val key = params.key ?: startPage
            val videoResponseAsync = if (isVideoChunkEnd) null else async {
                try {
                    api.getVideoList(
                        query = query,
                        sort = "recency",
                        page = key
                    )
                } catch (e: HttpException) {

                    // 해당 API response metadata에 isEnd가 내려오지 않고
                    // exception으로 페이징을 끝내는 경우가 있기 때문에
                    // null을 return하고 이를 마지막 페이지로 간주하도록 함
                    null
                } catch (e: Exception) {
                    throw e
                }
            }
            val imageResponseAsync = if (isImageChunkEnd) null else async {
                try {
                    api.getImageList(
                        query = query,
                        sort = "recency",
                        page = key
                    )
                } catch (e: HttpException) {
                    null
                } catch (e: Exception) {
                    throw e
                }
            }

            val videoResponse = videoResponseAsync?.await()
            val imageResponse = imageResponseAsync?.await()

            isVideoChunkEnd = videoResponse == null || videoResponse.meta.isEnd
            isImageChunkEnd = imageResponse == null || imageResponse.meta.isEnd

            if (isVideoChunkEnd && isImageChunkEnd) {
                return@supervisorScope LoadResult.Error(Exception("페이지가 더 이상 존재하지 않습니다"))
            }

            val nextKey = key + 1

            Timber.d("nextKey >> $nextKey")

            val videoChunk = videoResponse?.documents?.map { it.toDomainModel() } ?: emptyList()
            val imageChunk = imageResponse?.documents?.map { it.toDomainModel() } ?: emptyList()

            // 정렬은 paging chunk 단위로 수행된다.
            val mediaList = videoChunk.plus(imageChunk).sortedByDescending { it.timestamp }

            LoadResult.Page(
                data = mediaList,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(Exception("예상하지 못한 문제가 발생했어요.", e))
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