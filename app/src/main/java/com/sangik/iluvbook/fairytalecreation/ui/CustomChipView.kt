package com.sangik.iluvbook.fairytalecreation.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.sangik.iluvbook.R

class CustomChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val icon : ImageView
    private val chip : Chip

    init {
        // 커스텀 chip view 초기화
        LayoutInflater.from(context).inflate(R.layout.custom_chip, this, true)
        chip = findViewById(R.id.chip)
        icon = findViewById(R.id.icon)
        chip.checkedIcon = null
        chip.isCheckedIconVisible=false
        setInitialState() // 칩 초기 상태 설정
    }

    // Chip Text 설정
    fun setChipText(text: String) {
        chip.text = text
    }

    // 칩 초기 상태 설정
    private fun setInitialState() {
        icon.visibility = View.GONE // 초기 상태에서 아이콘 숨김
        chip.chipBackgroundColor = ContextCompat.getColorStateList(context, android.R.color.white)
        chip.setTextColor(ContextCompat.getColorStateList(context, R.color.chip_selected))
        chip.chipStrokeWidth=2f
    }

    // Chip 선택 시 상태
    fun setSelectedState(isFirst: Boolean) {
        icon.visibility = View.VISIBLE
        icon.setImageResource(if (isFirst) R.drawable.chip_icon_1 else R.drawable.chip_icon_2)
        chip.chipStrokeWidth=0f
        chip.chipBackgroundColor = ContextCompat.getColorStateList(context, android.R.color.black)
        chip.setTextColor(ContextCompat.getColorStateList(context, android.R.color.white))
    }

    // chip 선택 상태 초기화
    fun clearSelection() {
        setInitialState()
    }

    // 내부 chip 객체 반환
    fun getChip(): Chip {
        return chip
    }
}


