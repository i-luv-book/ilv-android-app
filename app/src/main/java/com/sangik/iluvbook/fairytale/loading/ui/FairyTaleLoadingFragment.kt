package com.sangik.iluvbook.fairytale.loading.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleLoadingBinding
import com.sangik.iluvbook.fairytale.loading.viewmodel.FairyTaleLoadingViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class FairyTaleLoadingFragment : Fragment() {
    private lateinit var binding: FragmentFairyTaleLoadingBinding
    private lateinit var fairyTaleLoadingViewModel : FairyTaleLoadingViewModel
    private val args: FairyTaleLoadingFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fairyTaleLoadingViewModel = ViewModelProvider(this).get(FairyTaleLoadingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fairy_tale_loading, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loadingViewModel = fairyTaleLoadingViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observeIntroViewModel()
        observeLoadingState() // 상태 관찰 시작
    }

    private fun initListener() {
        actionToFairyTaleIntroButton()
    }

    // 동화 생성 response observe
    private fun observeIntroViewModel() {
        val introViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
        introViewModel.apply {
            fairyTaleResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    fairyTaleLoadingViewModel.updateLoadingState() // 동화 생성 완료 UI로 업데이트
                }
            }
        }
    }

    // 로딩 상태 변화 관찰
    private fun observeLoadingState() {
        fairyTaleLoadingViewModel.isLoadingCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                updateLoadingBannerUi() // 제약 조건 및 애니메이션 적용
            }
        }
    }

    // 동화 인트로 이동
    private fun actionToFairyTaleIntroButton() {
        binding.btnReadFairytale.setOnClickListener {
            val fairyTaleResponse = ViewModelProvider(requireActivity())
                .get(IntroHangmanViewModel::class.java).fairyTaleResponse.value
            if (fairyTaleResponse != null) {
                val actionLoading = FairyTaleLoadingFragmentDirections
                    .actionFairyTaleLoadingFragmentToFairyTaleIntroFragment(fairyTaleResponse, args.keywords)
                findNavController().navigate(actionLoading)
            }
        }
    }

    private fun updateLoadingBannerUi() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.loadingLayout)

        constraintSet.connect(
            R.id.fairytale_loading_title,
            ConstraintSet.TOP,
            R.id.completed_icon,
            ConstraintSet.BOTTOM,
            32)

        // 부드러운 전환
        TransitionManager.beginDelayedTransition(binding.loadingLayout)

        // 새 제약 적용
        constraintSet.applyTo(binding.loadingLayout)
    }
}