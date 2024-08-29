package com.sangik.iluvbook.fairytale.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentOnboardingBinding
import com.sangik.iluvbook.fairytale.onboarding.viewmodel.OnboardingViewModel

class OnboardingFragment : Fragment() {
    private lateinit var vm : OnboardingViewModel

    private lateinit var binding : FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm = ViewModelProvider(requireActivity()).get(OnboardingViewModel::class.java)

        binding = DataBindingUtil.inflate<FragmentOnboardingBinding?>(
            inflater, R.layout.fragment_onboarding, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            onboardingViewModel = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        with(binding) {
            btnLow.setOnClickListener{ onLevelSelected("low") } // 난이도 선택 : 영유아
            btnMid.setOnClickListener{ onLevelSelected("mid") } // 난이도 선택 : 초등학교 저학년
            btnHigh.setOnClickListener{ onLevelSelected("high") } // 난이도 선택 : 초등학교 저학년
        }
    }
    private fun onLevelSelected(level : String) {
        vm.selectLevel(level)
        navigateToFairyTaleCreationFragment()
    }

    private fun navigateToFairyTaleCreationFragment() {
        findNavController().navigate(R.id.action_onboardingFragment_to_fairyTaleCreationFragment)
    }

}