package com.sangik.iluvbook.quiz.main.model

data class QuizTotalItem(
    val fairyTaleId : Long, // 동화 ID
    val title: String, // 동화 제목
    val correctQuizzesCount: Long // 맞은 퀴즈 수
)
