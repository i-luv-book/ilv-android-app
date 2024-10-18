package com.sangik.iluvbook.fairytale.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleLastResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.PageData
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse
import com.sangik.iluvbook.fairytale.model.dto.OptionUserSelected

class FairyTaleDetailViewModel : BaseViewModel() {
    private val _currentPageIndex = MutableLiveData<Int>()
    val currentPageIndex : LiveData<Int> get() = _currentPageIndex

    // 동화 Response 관리 리스트
    private val _pageDataList = MutableLiveData<MutableList<PageData>>()
    val pageDataList: LiveData<MutableList<PageData>> get() = _pageDataList

    // 동화 제목
    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle : LiveData<String> get() = _fairyTaleTitle

    // 왼쪽 버튼 노출 여부
    private val _isLeftButtonVisible = MutableLiveData<Boolean>()
    val isLeftButtonVisible: LiveData<Boolean> get() = _isLeftButtonVisible

    // 일반 페이지 여부
    private val _isGeneralPage = MutableLiveData<Boolean>()
    val isGeneralPage : LiveData<Boolean> get() = _isGeneralPage

    // 마지막 페이지 여부
    private val _isLastPage = MutableLiveData<Boolean>()
    val isLastPage : LiveData<Boolean> get() = _isLastPage

    // 선택 페이지 여부
    private val _isSelectionPage = MutableLiveData<Boolean>()
    val isSelectionPage: LiveData<Boolean> get() = _isSelectionPage

    //현재 페이지 타입
    private val _currentPageType = MutableLiveData<PageData>()
    val currentPageType: LiveData<PageData> get() = _currentPageType

    // 사용자 선택 옵션
    private val _selectedOption = MutableLiveData<OptionUserSelected?>()
    val selectedOption : LiveData<OptionUserSelected?> get() = _selectedOption


    init {
        _pageDataList.value = mutableListOf()
    }

    // 사용자 이전 선택 옵션 상세 정보 설정
    fun setSelectedOption(option : OptionUserSelected) {
        _selectedOption.value = option
    }

    // 선택 페이지 여부 업데이트 함수
    fun updatePageStatus(position: Int) {
        when (getPageDataAt(position)) {
            // 일반 동화인 경우
            is PageData.General -> {
                _isGeneralPage.value = true
                _isSelectionPage.value = false
                _isLastPage.value = false
            }
            // 선택형 동화인 경우
            is PageData.SelectionPage -> {
                _isSelectionPage.value = true
                _isGeneralPage.value = false
                _isLastPage.value = false
            }
            // 마지막 페이지인 경우
            is PageData.LastPage -> {
                _isLastPage.value = true
                _isSelectionPage.value = false
                _isGeneralPage.value = false
            }
            else -> {
                _isGeneralPage.value = false
                _isSelectionPage.value = false
                _isLastPage.value = false
            }
        }
    }

    // 새로운 선택형 동화 호출을 위해 이전 옵션, 동화 내용을 합하기
    fun createNewContent(): String {
        val currentIndex = getCurrentPageIndex()
        val previousContent = StringBuilder()

        for (i in 0 until currentIndex) {
            val pageData = getPageDataAt(i)
            if (pageData is PageData.General) {
                previousContent.append(pageData.content).append(". ")
            }
        }
        return previousContent.toString()
    }

    // 총 페이지 수 반환
    fun getTotalPageCount(): Int {
        return _pageDataList.value?.size ?: 0
    }

    // 특정 position의 페이지 데이터 반환
    fun getPageDataAt(position: Int): PageData? {
        return _pageDataList.value?.getOrNull(position)
    }

    // 페이지 타입 업데이트
    private fun updateCurrentPageType(position: Int) {
        _currentPageType.value = getPageDataAt(position)
    }

    // 현재 페이지 인덱스 반환
    fun getCurrentPageIndex() : Int {
        return currentPageIndex.value ?: 0
    }

    // 현재 페이지 인덱스 업데이트
    fun setCurrentPageIndex(index : Int) {
        _currentPageIndex.value = index
        updateCurrentPageType(index)
    }

    // 선택형 동화 추가
    fun addFairyTaleSelectionResponse(response: FairyTaleSelectionResponse) {
        // 이전의 선택 페이지 제거
        removeLastSelectionPage()
        // 동화 내용 페이지 추가
        _pageDataList.value?.add(
            PageData.General(response.title, response.content, response.imgURL)
        )

        // 옵션 추가
        _pageDataList.value?.add(PageData.SelectionPage(response.options))
    }

    fun addLastFairyTaleResponse(response : FairyTaleLastResponse) {
        // 이전 선택 페이지 제거
        removeLastSelectionPage()

        // 마지막 동화 내용 페이지 추가
        _pageDataList.value?.add(
            PageData.General(response.title, response.content, response.imgURL)
        )

        // 마지막 내용 추가
        _pageDataList.value?.add(
            PageData.LastPage(response.title)
        )

        updatePageStatus(3)
    }

    // 일반 동화 추가
    fun addFairyTaleResponse(response: FairyTaleResponse) {
        val page = response.pages.map {
            PageData.General(response.title, it.content, it.imgURL)
        }
        _pageDataList.value?.addAll(page)

        _pageDataList.value?.add(
            PageData.LastPage(
                title = response.title
            )
        )
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
        if (currentItem < getTotalPageCount() - 1) {
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }


    // 이전의 옵션 선택 페이지 제거
    private fun removeLastSelectionPage() {
        val list = _pageDataList.value
        if (!list.isNullOrEmpty() && list.last() is PageData.SelectionPage) {
            list.removeAt(list.size - 1)
        }
    }

    // 버튼 상태 변경
    fun updateSwipeButtonState(position : Int) {
        _isLeftButtonVisible.value = position > 0
        updatePageStatus(position)
    }

}