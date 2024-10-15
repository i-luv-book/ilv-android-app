package com.sangik.iluvbook.fairytale.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse

class FairyTaleIntroViewModel : BaseViewModel() {
    // 생성된 동화

    private val _fairyTaleImgUrl = MutableLiveData<String>()
    val fairyTaleImgUrl : LiveData<String> get() =  _fairyTaleImgUrl


    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle : LiveData<String> get() = _fairyTaleTitle

    private val _fairyTaleSummary = MutableLiveData<String>()
    val fairyTaleSummary : LiveData<String> get() = _fairyTaleSummary

    // 사용자 선택 모든 키워드
    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> get() = _keywords

    fun initData(fairyTaleTitle : String, fairyTaleSummary : String, imgUrl : String) {
        _fairyTaleTitle.value = fairyTaleTitle
        _fairyTaleSummary.value = fairyTaleSummary
        _fairyTaleImgUrl.value = imgUrl
    }
}