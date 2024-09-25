package com.sangik.iluvbook.fairytale.detail.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel

//ViewPager2의 각 페이지 관리 어댑터
class FairyTalePageAdapter(
    fragment : Fragment,
    private val vm : FairyTaleDetailViewModel
    ) : FragmentStateAdapter(fragment) {

    // ViewPager의 총 페이지 수를 반환
    override fun getItemCount(): Int {
        // FairyTaleDetailLastPage 를 위한 마지막 페이지 1개 더 추가
        return vm.pages.value?.size?.plus(1) ?: 0
    }

    override fun createFragment(position: Int): Fragment {
        val pageCount = vm.pages.value?.size ?: 0

        val fairyTaleTitle = vm.fairyTaleTitle.value.toString()
        return if (position < pageCount) {
            // 현재 position에 해당하는 Page 데이터를 가져와 FairyTalePageFragment를 생성
            val pageData = vm.pages.value?.get(position)
            FairyTalePageFragment.newInstance(pageData, fairyTaleTitle)
        } else {
            // 마지막 페이지일 경우 FairyTaleDetailLastPageFragment를 반환
            FairyTaleDetailLastPageFragment.newInstance(fairyTaleTitle)
        }
    }

}