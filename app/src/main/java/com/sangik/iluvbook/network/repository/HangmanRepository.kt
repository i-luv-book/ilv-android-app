package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.hangman.game.model.HangmanResponse
import com.sangik.iluvbook.network.service.HangmanService

class HangmanRepository(private val apiService: HangmanService) {
    suspend fun fetchWord(): HangmanResponse? {
        val response = apiService.getWord()
        if (response.isSuccessful) {
            return response.body()
        }else {
            // 에러 처리
            return null
        }
    }
}
