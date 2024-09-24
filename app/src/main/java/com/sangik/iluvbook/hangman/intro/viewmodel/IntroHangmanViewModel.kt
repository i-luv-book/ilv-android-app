package com.sangik.iluvbook.hangman.intro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleRequest
import com.sangik.iluvbook.fairytale.model.dto.FairyTaleResponse
import com.sangik.iluvbook.fairytale.model.dto.Keywords
import com.sangik.iluvbook.hangman.model.HangmanResponse
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.repository.FairyTaleRepository
import com.sangik.iluvbook.network.repository.HangmanRepository
import com.sangik.iluvbook.network.service.FairyTaleService
import kotlinx.coroutines.*

class IntroHangmanViewModel : BaseViewModel() {
    private val fairyTaleRepository: FairyTaleRepository
    private val hangmanRepository = HangmanRepository()

    private val _fairyTaleResponse = MutableLiveData<FairyTaleResponse>()
    val fairyTaleResponse: LiveData<FairyTaleResponse> get() = _fairyTaleResponse

    private val _hangmanResponse = MutableLiveData<HangmanResponse>()
    val hangmanResponse: LiveData<HangmanResponse> get() = _hangmanResponse

    init {
        val fairyTaleService: FairyTaleService = RetrofitClient.createService(FairyTaleService::class.java)
        fairyTaleRepository = FairyTaleRepository(fairyTaleService)
    }
    fun callHangmanAndFairyTaleApi(
        traits: List<String>,
        characters: List<String>,
        settings: List<String>,
        genre: List<String>,
        level: String
    ) {
        // Hangman API 호출
        viewModelScope.launch(Dispatchers.IO) {
            val response = hangmanRepository.fetchWord()
            _hangmanResponse.postValue(response)
        }

        // FairyTale API 호출
        viewModelScope.launch(Dispatchers.IO) {
            val response = createFairyTale(traits, characters, settings, genre, level)
            _fairyTaleResponse.postValue(response)
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

}
