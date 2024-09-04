package com.sangik.iluvbook.fairytale.model.dto

data class Keywords(
    val traits: List<String>, // 주인공
    val characters: List<String>, // 주인공 이름
    val settings: List<String>, // 배경
    val genre: List<String> // 장르
)
