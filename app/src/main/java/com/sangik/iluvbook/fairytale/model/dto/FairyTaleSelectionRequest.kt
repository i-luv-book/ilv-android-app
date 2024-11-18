package com.sangik.iluvbook.fairytale.model.dto

data class FairyTaleSelectionRequest (
    val keywords : Keywords, // 동화 생성 keywords
    val fairytale : String // 이전의 생성된 동화(옵션 포함)
)