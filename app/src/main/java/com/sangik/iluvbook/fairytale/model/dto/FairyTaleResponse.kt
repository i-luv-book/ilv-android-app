package com.sangik.iluvbook.fairytale.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * title 동화 제목
 * pages 동화 페이지
 * summary 동화 요약 */
@Parcelize
data class FairyTaleResponse(
    val title : String,
    val pages : List<Page>,
    val summary : String
) : Parcelable
