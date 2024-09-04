package com.sangik.iluvbook.hangman.intro.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentIntroHangmanBinding
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class IntroHangmanFragment : Fragment() {

    private lateinit var vm : IntroHangmanViewModel
    private lateinit var binding : FragmentIntroHangmanBinding
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(this).get(IntroHangmanViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_intro_hangman, container, false
        )

        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_introHangmanFragment_to_hangmanFragment)
        }
    }

}