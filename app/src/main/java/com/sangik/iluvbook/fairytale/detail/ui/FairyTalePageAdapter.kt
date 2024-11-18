package com.sangik.iluvbook.fairytale.detail.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
import com.sangik.iluvbook.fairytale.model.dto.PageData

class FairyTalePageAdapter(
    fragment : Fragment,
    private val fairyTaleDetailViewModel: FairyTaleDetailViewModel,
    ) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fairyTaleDetailViewModel.getTotalPageCount()
    }

    override fun createFragment(position: Int): Fragment {
        return when (val pageData = fairyTaleDetailViewModel.getPageDataAt(position)) {
            // 일반 동화
            is PageData.General -> {
                FairyTalePageFragment.newInstance(
                    fairyTaleTitle = pageData.title,
                    fairyTaleContent = pageData.content,
                    imgUrl = pageData.imgUrl.orEmpty())
            }
            // 선택형 동화
            is PageData.SelectionPage -> {
                FairyTaleSelectionFragment.newInstance(pageData.options)
            }

            // 마지막 페이지
            is PageData.LastPage -> {
                FairyTaleDetailLastPageFragment.newInstance(
                    fairyTaleTitle = pageData.title
                )
            }
            else -> {
                Fragment()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        // 해당 데이터 고유 ID
        return fairyTaleDetailViewModel.getPageDataAt(position)?.hashCode()?.toLong() ?: position.toLong()
    }

    // 특정 ID 페이지의 포함 여부
    override fun containsItem(itemId: Long): Boolean {
        return fairyTaleDetailViewModel.pageDataList.value?.any {
            it.hashCode().toLong() == itemId
        } ?: false
    }

}