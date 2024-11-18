package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleLastResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.network.service.FairyTaleLastService

class FairyTaleLastRepository(private val apiService : FairyTaleLastService) {
    suspend fun createLastFairyTale(request : FairyTaleSelectionRequest) : FairyTaleLastResponse? {
        val response = apiService.createFairyTaleLast(request)
        return if (response.isSuccessful) {
            response.body()
        }else {
            null
        }
    }
}