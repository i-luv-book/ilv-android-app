package com.sangik.iluvbook.network.repository

import android.util.Log
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.service.QuizListService
import com.sangik.iluvbook.network.service.QuizStatService
import com.sangik.iluvbook.quiz.main.model.QuizStatsData
import com.sangik.iluvbook.quiz.main.model.QuizTotalItem

class QuizMainRepository {
    private val quizStatService = RetrofitClient.createService(QuizStatService::class.java)
    private val quizListService = RetrofitClient.createService(QuizListService::class.java)

    // 퀴즈 통계 점수
    suspend fun getQuizStat(nickname: String): Result<QuizStatsData> {
        return try {
            val response = quizStatService.getQuizStatistic(nickname)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 전체 퀴즈 리스트
    suspend fun getQuizItems(nickname: String, fairytaleId: Long): Result<List<QuizTotalItem>> {
        return try {
            val response = quizListService.getQuizItems(nickname, fairytaleId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}