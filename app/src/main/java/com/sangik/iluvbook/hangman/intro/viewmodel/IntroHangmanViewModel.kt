package com.sangik.iluvbook.hangman.intro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleSelectionResponse
import com.sangik.iluvbook.fairytale.model.dto.Keywords
import com.sangik.iluvbook.hangman.model.HangmanResponse
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.repository.FairyTaleRepository
import com.sangik.iluvbook.network.repository.FairyTaleSelectionRepository
import com.sangik.iluvbook.network.repository.HangmanRepository
import com.sangik.iluvbook.network.service.FairyTaleSelectionService
import com.sangik.iluvbook.network.service.FairyTaleService
import kotlinx.coroutines.*

class IntroHangmanViewModel : BaseViewModel() {
    private val fairyTaleRepository: FairyTaleRepository
    private val fairyTaleSelectionRepository : FairyTaleSelectionRepository
    private val hangmanRepository = HangmanRepository()

    private val _fairyTaleResponse = MutableLiveData<FairyTaleResponse>()
    val fairyTaleResponse: LiveData<FairyTaleResponse> get() = _fairyTaleResponse

    private val _hangmanResponse = MutableLiveData<HangmanResponse>()
    private val _fairyTaleSelectionResponse = MutableLiveData<FairyTaleSelectionResponse>()
    val fairyTaleSelectionResponse : LiveData<FairyTaleSelectionResponse> get() = _fairyTaleSelectionResponse
    val hangmanResponse: LiveData<HangmanResponse> get() = _hangmanResponse
    private val _isPremium = MutableLiveData<Boolean>()
    val isPremium: LiveData<Boolean> get() = _isPremium

    private val _keywords = MutableLiveData<Keywords>()
    val keywords : LiveData<Keywords> get() = _keywords


    init {
        val fairyTaleService: FairyTaleService = RetrofitClient.createService(FairyTaleService::class.java)
        val fairyTaleSelectionService : FairyTaleSelectionService = RetrofitClient.createService(FairyTaleSelectionService::class.java)
        fairyTaleRepository = FairyTaleRepository(fairyTaleService)
        fairyTaleSelectionRepository = FairyTaleSelectionRepository(fairyTaleSelectionService)
    }
    fun callHangmanAndFairyTaleApi(
        traits: List<String>,
        characters: List<String>,
        settings: List<String>,
        genre: List<String>,
        level: String,
        isPremium : Boolean
    ) {
        // Hangman API 호출
        viewModelScope.launch(Dispatchers.IO) {
            val response = hangmanRepository.fetchWord()
            _hangmanResponse.postValue(response)
        }

        // premium 여부에 따른 동화 호출
        // premium : 선택형 동화 호출
        viewModelScope.launch(Dispatchers.IO) {
            if (isPremium) {
                val fairyTaleSelectionResponse = createFairyTaleSelection(traits, characters, settings, genre, "")
                _fairyTaleSelectionResponse.postValue(fairyTaleSelectionResponse)
            }else {
                val fairyTaleResponse = createFairyTale(traits, characters, settings, genre, level)
                _fairyTaleResponse.postValue(fairyTaleResponse)
            }
        }
    }

    private suspend fun createFairyTaleSelection(
        traits: List<String>,
        characters: List<String>,
        settings: List<String>,
        genre: List<String>,
        fairyTale : String
    ): FairyTaleSelectionResponse? {
        return try {
            fairyTaleSelectionRepository.createFairyTaleSelection(
                FairyTaleSelectionRequest(Keywords(traits, characters,settings,genre), fairyTale)
            )
        }catch (e: Exception) {
            null
        }
    }
    private suspend fun createFairyTale(
        traits: List<String>,
        characters: List<String>,
        settings: List<String>,
        genre: List<String>,
        level: String
    ): FairyTaleResponse? {
        return try {
            fairyTaleRepository.createFairyTale(
                level, FairyTaleRequest(Keywords(traits, characters, settings, genre))
            )
        } catch (e: Exception) {
            null
        }
    }

    // 사용자 선택 키워드 선택
    fun setKeywords(keywords : Keywords) {
        _keywords.value = keywords
    }

    // keywords 리스트 반환
    fun integrateKeywords(): List<String> {
        return keywords.value?.let { keywords ->
            keywords.traits + keywords.characters + keywords.settings + keywords.genre
        } ?: emptyList()
    }

    // 프리미엄 여부 설정
    fun setIsPremium(isPremium : Boolean) {
        _isPremium.value = isPremium
    }

}
