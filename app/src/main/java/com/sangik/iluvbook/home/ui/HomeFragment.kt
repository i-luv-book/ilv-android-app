package com.sangik.iluvbook.home.ui

import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.findNavController
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentHomeBinding
import com.sangik.iluvbook.home.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home,
    HomeViewModel::class,
    BR.viewModel
) {

    override fun initView() {}

    override fun initObserver() {
        viewModel.navigateToOnboarding.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_homeFragment_to_onboardingFragment)
                viewModel.onNavigateToOnBoarding()
            }
        }
    }
}
