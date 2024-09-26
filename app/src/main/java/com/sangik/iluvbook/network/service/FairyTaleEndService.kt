package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleEndResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FairyTaleEndService {
    // 선택형 동화 마지막 호출
    @POST("fairytale/game/end")
    suspend fun createFairyTaleEnd(
        @Body request : FairyTaleSelectionRequest
    ) : Response<FairyTaleEndResponse>
}