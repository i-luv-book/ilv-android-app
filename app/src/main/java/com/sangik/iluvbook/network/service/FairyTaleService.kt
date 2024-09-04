package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FairyTaleService {
    // 동화 생성
    @POST("fairytale/{difficulty}")
    suspend fun createFairyTale(
        @Path("difficulty") difficulty: String,
        @Body request: FairyTaleRequest
    ): Response<FairyTaleResponse>
}