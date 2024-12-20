package com.sangik.iluvbook.hangman.intro.ui

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentIntroHangmanBinding
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class IntroHangmanFragment : BaseFragment<FragmentIntroHangmanBinding, IntroHangmanViewModel>(
    R.layout.fragment_intro_hangman,
    IntroHangmanViewModel::class,
) {

    private val args: IntroHangmanFragmentArgs by navArgs()
    private lateinit var sharedViewModel: IntroHangmanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
    }

    override fun initView() {
        initializeData()
    }

    override fun initListener() {
        binding.btnStartGame.setOnClickListener { navigateToHangmanFragment() }
    }

    private fun initializeData() {
        // Premium 여부 설정
        sharedViewModel.setIsPremium(args.isPremium)

        // keywords 설정
        sharedViewModel.setKeywords(args.keywords)

        // 행맨, 동화 API 호출
        sharedViewModel.callHangmanAndFairyTaleApi(
            traits = args.keywords.traits,
            characters = args.keywords.characters,
            settings = args.keywords.settings,
            genre = args.keywords.genre,
            level = args.level,
            isPremium = args.isPremium
        )
    }
    private fun navigateToHangmanFragment() {
        val allKeywords = sharedViewModel.integrateKeywords().toTypedArray()
        val action = IntroHangmanFragmentDirections.actionIntroHangmanFragmentToHangmanFragment(allKeywords)
        findNavController().navigate(action)
    }
}