package com.sangik.iluvbook.network.service

import android.service.autofill.UserData
import com.sangik.iluvbook.quiz.main.model.UserProfileData
import retrofit2.http.GET
import retrofit2.http.Query

interface UserProfileService { // 유저 프로필 조회
    @GET("parent/child")
    suspend fun getUserProfile(
        @Query("nickname") nickname : String
    ): UserProfileData
}