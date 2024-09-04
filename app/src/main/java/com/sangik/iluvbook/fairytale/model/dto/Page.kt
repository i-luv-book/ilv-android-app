package com.sangik.iluvbook.fairytale.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Page( // page 객체
    val content: String,  // 페이지의 텍스트 내용
    val imgURL: String    // 페이지와 관련된 이미지 URL
) : Parcelable
