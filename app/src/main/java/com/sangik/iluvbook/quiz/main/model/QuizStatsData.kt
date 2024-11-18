package com.sangik.iluvbook.quiz.main.model

data class QuizStatsData(
    val solvedQuizzesTypesInfo: SolvedQuizzesTypesInfo, // 유형별 퀴즈 개수
    val solvedQuizzesCountsInfo: SolvedQuizzesCountsInfo // 맞춘 / 틀린 개수
)
