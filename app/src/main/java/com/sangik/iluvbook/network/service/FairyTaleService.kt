package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FairyTaleService {
    @POST("fairytale/{difficulty}")
    suspend fun createFairyTale(
        @Path("difficulty") difficulty: String,
        @Body request: FairyTaleRequest
    ): Call<FairyTaleResponse>
}