package com.sangik.iluvbook.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){
    private val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    protected fun setLoading(isLoading : Boolean) {
        this.isLoading.value = isLoading
    }
}