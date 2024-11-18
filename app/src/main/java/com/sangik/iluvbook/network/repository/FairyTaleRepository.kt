package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.network.service.FairyTaleService

class FairyTaleRepository(private val apiService: FairyTaleService) {
    suspend fun createFairyTale(difficulty: String, request: FairyTaleRequest): FairyTaleResponse? {
        val response = apiService.createFairyTale(difficulty, request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}