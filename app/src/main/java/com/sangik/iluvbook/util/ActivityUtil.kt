package com.sangik.iluvbook.util

import android.app.Activity
import android.content.Intent
import com.sangik.iluvbook.view.onboarding.OnboardingActivity

class ActivityUtil {
    fun startOnboardingActivity(activity: Activity){
        val intent = Intent(activity, OnboardingActivity::class.java)
        activity.startActivity(intent)
    }
}