package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse
import com.sangik.iluvbook.network.service.FairyTaleSelectionService

class FairyTaleSelectionRepository(private val apiService : FairyTaleSelectionService) {
    suspend fun createFairyTaleSelection(request : FairyTaleSelectionRequest) : FairyTaleSelectionResponse? {
        val response = apiService.createFairyTaleSelection(request)
        return if (response.isSuccessful) {
            response.body()
        }else {
            null
        }
    }
}