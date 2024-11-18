package com.sangik.iluvbook.fairytale.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel

class FairyTaleLastPageViewModel : BaseViewModel() {
    private val _fairyTaleTitle = MutableLiveData<String>()
    val fairyTaleTitle : LiveData<String> get() = _fairyTaleTitle

    fun setTitle(title : String) {
        _fairyTaleTitle.value = title
    }
}