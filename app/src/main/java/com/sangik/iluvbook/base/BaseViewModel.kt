package com.sangik.iluvbook.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){
    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> get() = _isLoading

    val errorMessage = MutableLiveData<String>()

    protected fun setLoading(isLoading : Boolean) {
        _isLoading.value = isLoading
    }
}