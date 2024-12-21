package com.sangik.iluvbook.fairytale.loading.ui

import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentFairyTaleLoadingBinding
import com.sangik.iluvbook.fairytale.loading.viewmodel.FairyTaleLoadingViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class FairyTaleLoadingFragment : BaseFragment<FragmentFairyTaleLoadingBinding, FairyTaleLoadingViewModel>(
    R.layout.fragment_fairy_tale_loading,
    FairyTaleLoadingViewModel::class
) {
    private lateinit var introViewModel: IntroHangmanViewModel
    private val args: FairyTaleLoadingFragmentArgs by navArgs()

    override fun initView() {
        introViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
    }

    override fun initObserver() {
        observeIntroViewModel()
        observeLoadingState()
    }

    override fun initListener() {
        binding.btnReadFairytale.setOnClickListener {
            handleFairyTaleIntroNavigation()
        }
    }

    // 동화 생성 response observe
    private fun observeIntroViewModel() {
        introViewModel.apply {
            fairyTaleResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    viewModel.updateLoadingState() // 동화 생성 완료 UI로 업데이트
                }
            }
            fairyTaleSelectionResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    viewModel.updateLoadingState()
                }
            }
        }
    }

    // 로딩 상태 변화 관찰
    private fun observeLoadingState() {
        viewModel.isLoadingCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                updateLoadingBannerUi() // 제약 조건 및 애니메이션 적용
            }
        }
    }

    // 동화 인트로 이동
    private fun handleFairyTaleIntroNavigation() {
        val fairyTaleResponse = introViewModel.fairyTaleResponse.value
        val fairyTaleSelectionResponse = introViewModel.fairyTaleSelectionResponse.value

        if (fairyTaleResponse != null || fairyTaleSelectionResponse != null) {
            val actionLoading = FairyTaleLoadingFragmentDirections
                .actionFairyTaleLoadingFragmentToFairyTaleIntroFragment(args.keywords)
            findNavController().navigate(actionLoading)
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
            32
        )

        // 부드러운 전환
        TransitionManager.beginDelayedTransition(binding.loadingLayout)

        // 새 제약 적용
        constraintSet.applyTo(binding.loadingLayout)
    }
}