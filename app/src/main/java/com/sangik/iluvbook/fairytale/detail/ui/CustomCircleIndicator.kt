package com.sangik.iluvbook.fairytale.detail.ui

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.R
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
import com.sangik.iluvbook.fairytale.model.dto.PageData
import me.relex.circleindicator.CircleIndicator3

class CustomCircleIndicator(context: Context, attrs: AttributeSet?) : CircleIndicator3(context, attrs) {

    // ViewModel, CustomCircleIndicator 연결, 페이지 상태 변화 감지
    fun bindViewModel(viewModel : FairyTaleDetailViewModel) {
        viewModel.currentPageType.observe((context as? LifecycleOwner)?: return) { pageData ->
            // 현재 페이지 타입 관찰
            updateIndicatorState(viewModel, viewModel.getCurrentPageIndex())
        }

        viewModel.currentPageIndex.observe((context as? LifecycleOwner) ?: return) { position ->
            updateIndicatorState(viewModel, position)
        }
    }

    override fun setViewPager(viewPager: ViewPager2?) {
        super.setViewPager(viewPager)
        updateIndicatorState(null, viewPager?.currentItem ?: 0)
    }

    // 인디케이터 상태 업데이트 함수
    internal fun updateIndicatorState(viewModel: FairyTaleDetailViewModel?, position: Int) {
        for (i in 0 until childCount) {
            val isSelectionPage = viewModel?.getPageDataAt(i) is PageData.SelectionPage
            updateIndicator(i, i == position, isSelectionPage)

        }
    }


    // Indicator UI 업데이트
    private fun updateIndicator(position: Int, isSelected: Boolean, isSelectionPage : Boolean) {
        val indicator = getChildAt(position)
        val layoutParams = indicator.layoutParams as MarginLayoutParams

        val (width, height, backgroundRes) = if (isSelected) { // 현재 보고있는 페이지 해당 Indicator
            if (isSelectionPage) { // 옵션 선택 페이지인 경우
                Triple(dpToPx(12), dpToPx(10), R.drawable.indicator_star_active) // 선택형 동화
            }else{ // 일반 동화 페이지인 경우
                Triple(dpToPx(22), dpToPx(10), R.drawable.indicator_selected) // 일반 동화
            }
        } else {
            if (isSelectionPage) {
                Triple(dpToPx(15), dpToPx(15), R.drawable.indicator_star_unselected)
            } else {
                Triple(dpToPx(12), dpToPx(12), R.drawable.indicator_unselected)
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