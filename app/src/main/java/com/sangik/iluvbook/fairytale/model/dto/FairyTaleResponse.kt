package com.sangik.iluvbook.fairytale.model.dto

data class FairyTaleResponse(
    val title : String, // 동화 제목
    val pages : List<Page>, // page List
    val summary : String // 동화 요약
)
