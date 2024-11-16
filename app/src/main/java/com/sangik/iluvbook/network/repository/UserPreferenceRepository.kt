package com.sangik.iluvbook.network.repository

import android.content.Context

class UserPreferenceRepository(private val context : Context) {
    private val preference = context.getSharedPreferences("user_preference", Context.MODE_PRIVATE)

    fun saveNickname(nickname: String) {
        preference.edit().putString("nickname", nickname).apply()
    }

    fun getNickname(): String {
        return preference.getString("nickname", "default_nickname") ?: "default_nickname"
    }
}