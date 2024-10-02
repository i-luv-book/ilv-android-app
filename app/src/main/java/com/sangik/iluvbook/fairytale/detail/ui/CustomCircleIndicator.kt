package com.sangik.iluvbook.fairytale.detail.ui

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.R
import me.relex.circleindicator.CircleIndicator3

class CustomCircleIndicator(context: Context, attrs: AttributeSet?) : CircleIndicator3(context, attrs) {
    private var isPremium: Boolean = false  // 선택형 동화 설정 플래그

    override fun setViewPager(viewPager: ViewPager2?) {
        super.setViewPager(viewPager)
        updateIndicatorState(viewPager?.currentItem ?: 0)
    }

    fun setIsPremium(isPremium : Boolean) {
        this.isPremium = isPremium
    }

    internal fun updateIndicatorState(position: Int) {
        for (i in 0 until childCount) {
            updateIndicator(i, i == position)  // 선택된 인디케이터는 true, 나머지는 false
        }
    }

    // Indicator UI 업데이트
    private fun updateIndicator(position: Int, isSelected: Boolean) {
        val indicator = getChildAt(position)
        val layoutParams = indicator.layoutParams as MarginLayoutParams

        val (width, height, backgroundRes) = if (isSelected) {
            // 선택된 인디케이터
            if (isPremium && (position == 1 || position == 3 || position == 5)) {
                Triple(dpToPx(12), dpToPx(10), R.drawable.indicator_star_active) // 선택형 동화
            } else {
                Triple(dpToPx(22), dpToPx(10), R.drawable.indicator_selected) // 일반 동화
            }
        } else {
            // 선택되지 않은 인디케이터
            if (isPremium && (position == 2 || position == 4 || position == 6)) {
                Triple(dpToPx(12), dpToPx(10), R.drawable.indicator_star_unselected) // 선택형 동화
            } else {
                Triple(dpToPx(12), dpToPx(12), R.drawable.indicator_unselected) // 일반 동화
            }
        }


        indicator.setBackgroundResource(backgroundRes)
        layoutParams.setMargins(dpToPx(8), 0, dpToPx(8), 0)
        layoutParams.width = width
        layoutParams.height = height
        indicator.layoutParams = layoutParams
    }


    // dp -> px 변환 함수
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}