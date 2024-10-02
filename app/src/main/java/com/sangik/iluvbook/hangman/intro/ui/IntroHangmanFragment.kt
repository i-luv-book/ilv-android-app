package com.sangik.iluvbook.hangman.intro.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentIntroHangmanBinding
import com.sangik.iluvbook.fairytale.model.dto.Keywords
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel
class IntroHangmanFragment : Fragment() {

    private lateinit var vm : IntroHangmanViewModel
    private lateinit var binding : FragmentIntroHangmanBinding
    private val args: IntroHangmanFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_intro_hangman, container, false
        )

        setupKeywords()
        initIsPremiumStatus()
        generateHangmanAndFairyTale()
        initListener()

        return binding.root
    }

    private fun initIsPremiumStatus() {
        vm.setIsPremium(args.isPremium)
    }

    // 사용자 선택 keywords 설정
    private fun setupKeywords() {
        vm.setKeywords(args.keywords)
    }

    private fun initListener() {
        binding.btnStartGame.setOnClickListener {
            val allKeywords = vm.integrateKeywords().toTypedArray()
            val introHangmanAction = IntroHangmanFragmentDirections.actionIntroHangmanFragmentToHangmanFragment(allKeywords)
            findNavController().navigate(introHangmanAction)
        }
    }


    private fun generateHangmanAndFairyTale() {
        vm.callHangmanAndFairyTaleApi(
            traits = args.keywords.traits,
            characters = args.keywords.characters,
            settings = args.keywords.settings,
            genre = args.keywords.genre,
            level = args.level,
            args.isPremium
        )
    }
}

