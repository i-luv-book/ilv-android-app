package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.quiz.main.model.QuizTotalItem
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizListService {
    @POST("education/quizzes")
    suspend fun getQuizItems(
        @Query("nickname") nickname: String,
        @Query("fairytaleId") fairytaleId: Long
    ): List<QuizTotalItem>
}