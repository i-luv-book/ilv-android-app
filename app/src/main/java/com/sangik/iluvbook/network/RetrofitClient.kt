package com.sangik.iluvbook.network

import com.sangik.iluvbook.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = BuildConfig.BASE_URL// 기본 URL을 설정

    // 타임아웃 설정 추가
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS) // 연결 타임아웃
        .readTimeout(120, TimeUnit.SECONDS)    // 읽기 타임아웃
        .writeTimeout(120, TimeUnit.SECONDS)   // 쓰기 타임아웃
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}