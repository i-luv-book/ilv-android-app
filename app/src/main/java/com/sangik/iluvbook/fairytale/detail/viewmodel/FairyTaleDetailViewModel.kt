package com.sangik.iluvbook.fairytale.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.Page

class FairyTaleDetailViewModel : BaseViewModel() {
    // 동화 생성 응답의 page 데이터
    private val _pages = MutableLiveData<List<Page>>()
    val pages: LiveData<List<Page>> get() = _pages

    // 동화 제목
    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle : LiveData<String> get() = _fairyTaleTitle

    // 왼쪽 버튼 노출 여부
    private val _isLeftButtonVisible = MutableLiveData<Boolean>()
    val isLeftButtonVisible: LiveData<Boolean> get() = _isLeftButtonVisible


    // 마지막 페이지 여부
    private val _isLastPage = MutableLiveData<Boolean>()
    val isLastPage : LiveData<Boolean> get() = _isLastPage

    // 전체 아이템 개수
    private var totalItem : Int = 0

    // 전체 아이템 수 설정
    fun setTotalItem(count : Int) {
        totalItem = count
    }

    // ViewPager 좌측 스와이프
    fun swipeLeft(viewPager : ViewPager2) {
        val currentItem = viewPager.currentItem
        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1, true)
        }
    }

    // ViewPager 우측 스와이프
    fun swipeRight(viewPager : ViewPager2) {
        val currentItem = viewPager.currentItem
        if (currentItem < totalItem - 1) {
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }

    // 버튼 상태 변경
    fun updateSwipeButtonState(position : Int) {
        _isLeftButtonVisible.value = position > 0
    }

    // 마지막 페이지 상태 업데이트
    fun updateLastPageState(position : Int) {
        _isLastPage.value = position == totalItem - 1
    }

    // 동화 생성 데이터 설정
    fun setPages(pages: List<Page>) {
        _pages.value = pages
    }

    // 동화 제목
    fun setFairyTaleTitle(title : String) {
        _fairyTaleTitle.value = title
    }
}