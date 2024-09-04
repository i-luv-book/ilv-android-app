package com.sangik.iluvbook.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val _navigateToOnboarding = MutableLiveData(false)
    val navigateToOnboarding : LiveData<Boolean> get() = _navigateToOnboarding

    fun onMoveTextClicked() {
        _navigateToOnboarding.value = true
    }

    fun onNavigateToOnBoarding() {
        _navigateToOnboarding.value = false
    }
}