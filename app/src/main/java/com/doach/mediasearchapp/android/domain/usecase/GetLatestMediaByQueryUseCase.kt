package com.doach.mediasearchapp.android.domain.usecase

import com.doach.mediasearchapp.android.domain.model.Media

@Deprecated(
    message = "테스트를 위해 작성됨, GetMediaFlowByQueryUseCase()를 사용할 것",
    replaceWith = ReplaceWith("GetMediaFlowByQueryUseCase()")
)
interface GetLatestMediaByQueryUseCase {
    suspend operator fun invoke(query: String): List<Media>
}