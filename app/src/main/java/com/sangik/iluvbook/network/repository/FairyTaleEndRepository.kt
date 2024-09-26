package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleEndResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.network.service.FairyTaleEndService

class FairyTaleEndRepository(private val apiService : FairyTaleEndService) {
    suspend fun createFairyTaleEnd(request : FairyTaleSelectionRequest) : FairyTaleEndResponse? {
        val response = apiService.createFairyTaleEnd(request)
        return if (response.isSuccessful) {
            response.body()
        }else {
            null
        }
    }
}