package com.sangik.iluvbook.fairytale.model.dto

sealed class PageData {
    data class General(val title: String, val content: String, val imgUrl: String?) : PageData()
    data class SelectionPage(val options: List<FairyTaleOption>) : PageData()
    data class LastPage(val title : String) : PageData()
}