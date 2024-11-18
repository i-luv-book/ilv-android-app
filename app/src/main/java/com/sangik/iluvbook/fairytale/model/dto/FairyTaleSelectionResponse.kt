package com.sangik.iluvbook.fairytale.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FairyTaleSelectionResponse (
    val title : String,
    val content : String,
    val options : List<FairyTaleOption>,
    val imgURL : String
) : Parcelable