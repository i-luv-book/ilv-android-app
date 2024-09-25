package com.sangik.iluvbook.fairytale.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse

class FairyTaleIntroViewModel : BaseViewModel() {
    // 생성된 동화

    private val _fairyTaleResponse = MutableLiveData<FairyTaleResponse>()
    val fairyTaleResponse: LiveData<FairyTaleResponse> get() = _fairyTaleResponse


    // 사용자 선택 모든 키워드
    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> get() = _keywords

    fun initData(response: FairyTaleResponse, keywords : List<String>) {
        _fairyTaleResponse.value = response
        _keywords.value = keywords
    }
}