package com.sangik.iluvbook.fairytale.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FairyTaleEndResponse(
    val title : String, // 게임형 동화 제목
    val content : String, // 게임형 동화 마지막 내용
    val imgURL : String // 동화 이미지
) : Parcelable
