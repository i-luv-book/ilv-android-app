package com.sangik.iluvbook.network.repository

import com.sangik.iluvbook.hangman.model.HangmanResponse
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.service.HangmanService


class HangmanRepository {

    private val hangmanService: HangmanService = RetrofitClient.createService(HangmanService::class.java)

    suspend fun fetchWord(): HangmanResponse? {
        val response = hangmanService.getWord()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
