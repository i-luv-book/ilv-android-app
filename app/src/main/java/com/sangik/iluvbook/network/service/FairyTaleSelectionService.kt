package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FairyTaleSelectionService {
    // 선택형 동화 생성
    @POST("fairytale/game/select")
    suspend fun createFairyTaleSelection(
        @Body request : FairyTaleSelectionRequest
    ) : Response<FairyTaleSelectionResponse>
}
