package com.doach.mediasearchapp.android.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doach.mediasearchapp.android.data.remote.dto.toDomainModel
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.domain.model.Image

class ImagePagingSource(
    private val api: ApiService,
    private val query: String
): PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        return try {
            val nextPageNumber = params.key ?: startPage
            val response = api.getImageList(
                query = query,
                sort = "recency",
                page = nextPageNumber
            )

            val nextKey = if (response.meta.isEnd) null else nextPageNumber + 1

            LoadResult.Page(
                data = response.documents.map { it.toDomainModel() },
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val startPage = 1
    }
}