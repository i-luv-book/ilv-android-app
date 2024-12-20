package com.sangik.iluvbook.fairytale.onboarding.ui

import androidx.navigation.fragment.findNavController
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentOnboardingBinding
import com.sangik.iluvbook.fairytale.onboarding.viewmodel.OnboardingViewModel

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding, OnboardingViewModel>(
    R.layout.fragment_onboarding,
    OnboardingViewModel::class
) {

    override fun initListener() {
        with(binding) {
            btnLow.setOnClickListener{ navigateToFairyTaleCreationFragment("low") } // 난이도 선택 : 영유아
            btnMid.setOnClickListener{ navigateToFairyTaleCreationFragment("mid") } // 난이도 선택 : 초등학교 저학년
            btnHigh.setOnClickListener{ navigateToFairyTaleCreationFragment("high") } // 난이도 선택 : 초등학교 저학년
        }
    }
    private fun navigateToFairyTaleCreationFragment(selectedLevel : String) {
        val actionToFairyTaleCreation = OnboardingFragmentDirections
            .actionOnboardingFragmentToFairyTaleCreationFragment(selectedLevel)
        findNavController().navigate(actionToFairyTaleCreation)
    }
}