package com.doach.mediasearchapp.android.domain.usecase

import androidx.paging.PagingData
import com.doach.mediasearchapp.android.domain.model.MediaWithFavorite
import kotlinx.coroutines.flow.Flow

interface GetMediaFlowByQueryUseCase {

    suspend operator fun invoke(query: String): Flow<PagingData<MediaWithFavorite>>
}