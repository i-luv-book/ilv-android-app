package com.sangik.iluvbook.onboarding.ui

import android.os.Bundle
import android.view.View
import com.sangik.iluvbook.BR
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseActivity
import com.sangik.iluvbook.databinding.ActivityOnboardingBinding
import com.sangik.iluvbook.fairytale.creation.ui.FairyTaleCreationFragment
import com.sangik.iluvbook.onboarding.viewmodel.OnboardingViewModel

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding, OnboardingViewModel>(
    R.layout.activity_onboarding,
    OnboardingViewModel::class,
    BR.onboardingViewModel
) {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun initListener() {
        super.initListener()

        // 난이도 선택 : 영유아
        binding.btnLow.setOnClickListener{
            viewModel.selectLevel("low")
            navigateToFairyTaleCreationFragment()
        }

        // 난이도 선택 : 초등학교 저학년
        binding.btnMid.setOnClickListener{
            viewModel.selectLevel("mid")
            navigateToFairyTaleCreationFragment()
        }

        // 난이도 선택 : 초등학교 고학년
        binding.btnHigh.setOnClickListener{
            viewModel.selectLevel("high")
            navigateToFairyTaleCreationFragment()
        }
    }

    private fun navigateToFairyTaleCreationFragment(){
        val fragment = FairyTaleCreationFragment()

        binding.onboardingScroll.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.onboarding_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}