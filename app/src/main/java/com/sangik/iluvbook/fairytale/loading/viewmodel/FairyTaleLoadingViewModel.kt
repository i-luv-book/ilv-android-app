package com.sangik.iluvbook.fairytale.loading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel

class FairyTaleLoadingViewModel : BaseViewModel() {

    private val _isLoadingCompleted = MutableLiveData(false)
    val isLoadingCompleted: LiveData<Boolean> get() = _isLoadingCompleted

    // 동화 완료 업데이트
    fun updateLoadingState() {
        _isLoadingCompleted.value = true
    }
}