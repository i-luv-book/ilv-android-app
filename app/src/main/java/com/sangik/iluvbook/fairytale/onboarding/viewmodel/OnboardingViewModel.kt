package com.sangik.iluvbook.fairytale.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel

class OnboardingViewModel : BaseViewModel() {
    private val _selectedLevel = MutableLiveData<String>()
    val selectedLevel : LiveData<String> get() = _selectedLevel

    fun selectLevel(option : String){
        _selectedLevel.value = option
    }

}