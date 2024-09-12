package com.sangik.iluvbook.fairytale.detail.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.util.TextTranslator

class FairyTalePageViewModel : BaseViewModel() {
    private lateinit var contentTranslator: TextTranslator

    // 번역 버튼 상태 관리
    private val _isTranslationButtonActive = MutableLiveData<Boolean>(false)
    val isTranslationButtonActive: LiveData<Boolean> get() = _isTranslationButtonActive

    // TTS 버튼 상태 관리
    private val _isTTSButtonActive = MutableLiveData<Boolean>(false)
    val isTTSButtonActive: LiveData<Boolean> get() = _isTTSButtonActive

    // 현재 페이지의 텍스트 관리 (번역 또는 원본)
    private val _pageContent = MutableLiveData<String>()
    val pageContent: LiveData<String> get() = _pageContent

    // 동화 타이틀
    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle : LiveData<String> get() = _fairyTaleTitle

    // 원본 (영문 동화)
    private var originalText: String = ""

    // 동화 제목
    fun setFairyTaleTitle(title : String) {
        _fairyTaleTitle.value = title
    }

    fun setOriginalText(text: String) {
        originalText = text
        _pageContent.value = if (_isTranslationButtonActive.value == true) text else originalText
    }

    // 번역 초기화
    fun initTranslator() {
        contentTranslator = TextTranslator()
        contentTranslator.initialize({}, {})
    }

    // 번역
    fun translateFairyTale() {
        contentTranslator.translate(
            originalText,
            { translatedText -> _pageContent.value = translatedText },
            { exception -> _pageContent.value = originalText } // 예외 : 원본
        )
    }

    // 원본 동화로 설정. (영문)
    fun setOriginalFairyTale() {
        _pageContent.value = originalText
    }

    // ViewModel이 소멸될 때 호출되는 메소드
    override fun onCleared() {
        super.onCleared()
        contentTranslator.close() // 번역 리소스 해제
    }

    // 번역 버튼 상태 토글
    fun toggleTranslateButton() {
        _isTranslationButtonActive.value = _isTranslationButtonActive.value?.not()
    }

    // TTS 버튼 상태 토글
    fun toggleTTSButton() {
        _isTTSButtonActive.value = _isTTSButtonActive.value?.not()
    }

    fun deactivateAllStates() {
        _isTranslationButtonActive.value = false
        _isTTSButtonActive.value = false
    }
}