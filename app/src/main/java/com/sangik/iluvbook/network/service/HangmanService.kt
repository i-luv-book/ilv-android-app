package com.sangik.iluvbook.network.service

import com.sangik.iluvbook.hangman.game.model.HangmanResponse
import retrofit2.Response
import retrofit2.http.GET

interface HangmanService {
    @GET("hangman/word")
    suspend fun getWord(): Response<HangmanResponse>
}