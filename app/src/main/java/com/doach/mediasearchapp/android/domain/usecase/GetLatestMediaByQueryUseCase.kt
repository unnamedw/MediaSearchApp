package com.doach.mediasearchapp.android.domain.usecase

import com.doach.mediasearchapp.android.domain.model.Media

interface GetLatestMediaByQueryUseCase {
    suspend operator fun invoke(query: String): List<Media>
}