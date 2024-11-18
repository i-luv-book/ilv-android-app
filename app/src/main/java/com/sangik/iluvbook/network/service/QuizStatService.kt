package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.quiz.main.model.QuizStatsData
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizStatService { // 퀴즈 통계 조회
    @POST("education/quizzes/statistics")
    suspend fun getQuizStatistic(
        @Query("nickname") nickname: String
    ): QuizStatsData
}