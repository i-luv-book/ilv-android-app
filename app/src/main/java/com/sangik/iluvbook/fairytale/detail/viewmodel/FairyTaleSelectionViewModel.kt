package com.sangik.iluvbook.fairytale.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleLastResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleOption
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse
import com.sangik.iluvbook.fairytale.model.dto.Keywords
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.repository.FairyTaleLastRepository
import com.sangik.iluvbook.network.repository.FairyTaleSelectionRepository
import com.sangik.iluvbook.network.service.FairyTaleLastService
import com.sangik.iluvbook.network.service.FairyTaleSelectionService
import com.sangik.iluvbook.util.TextTranslator
import kotlinx.coroutines.launch

class FairyTaleSelectionViewModel : BaseViewModel() {
    private lateinit var contentTranslator: TextTranslator

    // 로딩 상태를 나타내는 LiveData
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    private var fairyTaleSelectionRepository : FairyTaleSelectionRepository
    private var fairyTaleLastRepository : FairyTaleLastRepository

    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle: LiveData<String> get() = _fairyTaleTitle

    private val _fairyTaleSelectionResponseList = MutableLiveData<MutableList<FairyTaleSelectionResponse>>(mutableListOf())
    val fairyTaleSelectionResponseList: LiveData<MutableList<FairyTaleSelectionResponse>> get() = _fairyTaleSelectionResponseList


    private val _fairyTaleLastResponse = MutableLiveData<FairyTaleLastResponse>()
    val fairyTaleLastResponse: LiveData<FairyTaleLastResponse> get() = _fairyTaleLastResponse

    private val _options = MutableLiveData<List<FairyTaleOption>>()
    val options: LiveData<List<FairyTaleOption>> get() = _options

    private val _selectedOptionIndex = MutableLiveData<Int>()
    val selectedOptionIndex: LiveData<Int> get() = _selectedOptionIndex


    private val _optionATranslated = MutableLiveData<String>()
    val optionATranslated: LiveData<String> get() = _optionATranslated

    private val _optionBTranslated = MutableLiveData<String>()
    val optionBTranslated: LiveData<String> get() = _optionBTranslated

    private val _optionCTranslated = MutableLiveData<String>()
    val optionCTranslated: LiveData<String> get() = _optionCTranslated


    init {
        _fairyTaleSelectionResponseList.value = mutableListOf()
        val fairyTaleSelectionService : FairyTaleSelectionService = RetrofitClient.createService(
            FairyTaleSelectionService::class.java)
        fairyTaleSelectionRepository = FairyTaleSelectionRepository(fairyTaleSelectionService)

        val fairyTaleLastService : FairyTaleLastService = RetrofitClient.createService(
            FairyTaleLastService::class.java
        )
        fairyTaleLastRepository = FairyTaleLastRepository(fairyTaleLastService)

        initTranslator()
    }

    // 옵션 선택 정보 업데이트
    fun toggleOptionSelection(index : Int) {
        _selectedOptionIndex.value = if (_selectedOptionIndex.value == index) -1 else index
    }

    // 옵션 초기화
    fun initOptions(options : List<FairyTaleOption>) {
        _options.value = removeLineBreak(options)
        _selectedOptionIndex.value = -1
        translateOptionContents()
    }

    // 선택형 동화 호출 (옵션 포함)
    fun callNewSelectionFairyTale(keywords: Keywords, newContent : String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = createFairyTaleSelection(keywords, newContent)

            response?.let { newResponse ->
                _fairyTaleSelectionResponseList.value = _fairyTaleSelectionResponseList.value?.apply {
                    add(newResponse)
                } ?: mutableListOf(newResponse)
            }

            _isLoading.value = false
        }
    }

    // 마지막 선택형 동화 호출호출
    fun callLastSelectionFairyTale(keywords: Keywords, newContent: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = createLastFairyTaleSelection(keywords, newContent)

            response?.let {
                _fairyTaleLastResponse.value = it
            }
            _isLoading.value = false
        }
    }

    // 선택형 동화 옵션 정보 설정
    fun setupNewSelectionOption(response: FairyTaleSelectionResponse) {
        _fairyTaleTitle.value = response.title
        _options.value = removeLineBreak(response.options)
        translateOptionContents()
    }

    // 줄바꿈 제거
    private fun removeLineBreak(options: List<FairyTaleOption>): List<FairyTaleOption> {
        return options.map { option ->
            FairyTaleOption(
                optionTitle = option.optionTitle,
                optionContent = option.optionContent.replace("\n", "") // 줄바꿈 제거
            )
        }
    }


    // 선택형 동화 호출
    private suspend fun createFairyTaleSelection(
        keywords: Keywords,
        fairyTale: String
    ): FairyTaleSelectionResponse? {
        return try {
            fairyTaleSelectionRepository.createFairyTaleSelection(
                FairyTaleSelectionRequest(keywords, fairyTale)
            )
        } catch (e: Exception) {
            null
        }
    }

    // 번역 초기화
    private fun initTranslator() {
        contentTranslator = TextTranslator()
        contentTranslator.initialize({}, {})
    }

    // 옵션 내용 변역
    private fun translateOptionContents() {
        val originalOptionContentA = options.value?.get(0)?.optionContent.toString()
        val originalOptionContentB = options.value?.get(1)?.optionContent.toString()
        val originalOptionContentC = options.value?.get(2)?.optionContent.toString()

        contentTranslator.translate(
            originalOptionContentA,
            { translatedText -> _optionATranslated.value = translatedText},
            { exception -> _optionATranslated.value = ""}
        )

        contentTranslator.translate(
            originalOptionContentB,
            { translatedText -> _optionBTranslated.value = translatedText},
            { exception -> _optionBTranslated.value = ""}
        )

        contentTranslator.translate(
            originalOptionContentC,
            { translatedText -> _optionCTranslated.value = translatedText},
            { exception -> _optionCTranslated.value = ""}
        )
    }

    // 마지막 선택형 동화
    private suspend fun createLastFairyTaleSelection(
        keywords: Keywords,
        fairyTale: String
    ) : FairyTaleLastResponse? {
        return try {
            fairyTaleLastRepository.createLastFairyTale(
                FairyTaleSelectionRequest(keywords, fairyTale)
            )
        } catch (e: Exception) {
            null
        }
    }
}
