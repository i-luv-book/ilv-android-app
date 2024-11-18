package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.service.UserProfileService
import com.sangik.iluvbook.quiz.main.model.UserProfileData

class UserProfileRepository {
    private val userProfileService = RetrofitClient.createService(UserProfileService::class.java)

    suspend fun getProfileData(nickname: String): Result<UserProfileData> {
        return try {
            val response = userProfileService.getUserProfile(nickname)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}