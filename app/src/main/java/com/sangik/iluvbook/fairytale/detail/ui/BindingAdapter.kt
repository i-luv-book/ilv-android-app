package com.sangik.iluvbook.fairytale.detail.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sangik.iluvbook.R

@BindingAdapter("optionIcon")
fun setOptionIcon(view: ImageView, optionNumber: Int?) {
    val iconRes = when (optionNumber) {
        0 -> R.drawable.option_a
        1 -> R.drawable.option_b
        2 -> R.drawable.option_c
        else -> R.drawable.option_a
    }
    view.setBackgroundResource(iconRes)
}