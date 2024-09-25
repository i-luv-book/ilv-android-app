package com.sangik.iluvbook.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
}