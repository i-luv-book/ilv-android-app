package com.sangik.iluvbook.fairytale.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FairyTaleOption (
    val optionTitle : String, // 사용자 동화 선택 옵션 타이틀
    val optionContent : String // 사용자 동화 선택 옵션 내용
) : Parcelable