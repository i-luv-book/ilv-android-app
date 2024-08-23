package com.sangik.iluvbook.hangman.game.model

import retrofit2.Response
import retrofit2.http.GET

interface HangmanService {
    @GET("hangman/word")
    suspend fun getWord(): Response<HangmanResponse>
}