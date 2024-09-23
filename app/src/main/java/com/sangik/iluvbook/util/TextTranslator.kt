package com.sangik.iluvbook.util

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslatorOptions

class TextTranslator {
    private var translator: Translator? = null
    private var originalText: String = ""

    fun initialize(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.KOREAN)
            .build()

        translator = com.google.mlkit.nl.translate.Translation.getClient(options)

        // 모델 다운로드
        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator?.downloadModelIfNeeded(conditions)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onFailure(it) }
    }

    // 번역
    fun translate(text: String, onTranslated: (String) -> Unit, onFailure: (Exception) -> Unit) {
        originalText = text
        translator?.translate(text)
            ?.addOnSuccessListener { onTranslated(it) }
            ?.addOnFailureListener { onFailure(it) }
    }

    fun close() {
        translator?.close()
    }
}