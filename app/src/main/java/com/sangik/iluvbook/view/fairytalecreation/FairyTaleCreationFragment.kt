package com.sangik.iluvbook.view.fairytalecreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleCreationBinding
import com.sangik.iluvbook.view.onboarding.OnboardingViewModel

class FairyTaleCreationFragment : Fragment() {

    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var fairyTaleCreationViewModel: FairyTaleCreationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // OnboardingActivity와 공유
        onboardingViewModel = ViewModelProvider(requireActivity()).get(OnboardingViewModel::class.java)

        val binding : FragmentFairyTaleCreationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_creation, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        onboardingViewModel.selectedLevel.observe(viewLifecycleOwner) { level ->
            binding.selectedLevel.text = level
        }

        return binding.root
    }
}