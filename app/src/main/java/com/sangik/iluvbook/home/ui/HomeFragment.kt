package com.sangik.iluvbook.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentHomeBinding
import com.sangik.iluvbook.home.viewmodel.HomeViewModel
import com.sangik.iluvbook.fairytale.onboarding.ui.OnboardingFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initObserver()

        return binding.root
    }
    private fun initObserver() {
        homeViewModel.navigateToOnboarding.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_homeFragment_to_onboardingFragment)
                homeViewModel.onNavigateToOnBoarding()
            }
        }
    }
}