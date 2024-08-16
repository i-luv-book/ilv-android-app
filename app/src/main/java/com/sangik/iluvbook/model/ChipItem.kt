package com.sangik.iluvbook.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class ChipItem (
    val text : String,
    @get:Bindable var isSelected: Boolean = false
) : BaseObservable() {
    fun toggleSelection() {
        isSelected = !isSelected
    }
}