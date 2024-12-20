    package com.sangik.iluvbook.fairytale.detail.ui

    import android.os.Bundle
    import androidx.lifecycle.ViewModelProvider
    import com.sangik.iluvbook.R
    import com.sangik.iluvbook.base.BaseFragment
    import com.sangik.iluvbook.databinding.FragmentFairyTaleSelectionBinding
    import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
    import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleSelectionViewModel
    import com.sangik.iluvbook.fairytale.model.dto.FairyTaleOption
    import com.sangik.iluvbook.fairytale.model.dto.OptionUserSelected

    class FairyTaleSelectionFragment : BaseFragment<FragmentFairyTaleSelectionBinding, FairyTaleSelectionViewModel>(
        R.layout.fragment_fairy_tale_selection,
        FairyTaleSelectionViewModel::class
    ) {
        private lateinit var detailViewModel : FairyTaleDetailViewModel
        private lateinit var options: List<FairyTaleOption>

        override fun initView() {
            options = arguments?.getParcelableArrayList(ARG_OPTION) ?: emptyList()
            detailViewModel = ViewModelProvider(requireActivity()).get(FairyTaleDetailViewModel::class.java)
            viewModel.initOptions(options)
        }

        override fun initListeners() {
            binding.optionACard.setOnClickListener {
                viewModel.toggleOptionSelection(0)
            }
            binding.optionBCard.setOnClickListener {
                viewModel.toggleOptionSelection(1)
            }
            binding.optionCCard.setOnClickListener {
                viewModel.toggleOptionSelection(2)
            }
        }

        override fun initObserver() {
            // 옵션 카드 상태 변경
            viewModel.selectedOptionIndex.observe(viewLifecycleOwner) { selectedIndex ->
                updateOptionCardBackgrounds(selectedIndex)
                setUserSelectedOption(selectedIndex)
            }

            // 사용자 선택 후 새 동화 관련 Response Observing
            viewModel.fairyTaleSelectionResponseList.observe(viewLifecycleOwner) { response ->
                response?.let {
                    if (it.isNotEmpty()) {
                        viewModel.setupNewSelectionOption(it.last())
                    }
                }
            }
        }

        private fun setUserSelectedOption(selectedIndex: Int) {
            if (selectedIndex != -1) {
                val selectedOption = options[selectedIndex]

                // 번역된 옵션 내용 설정
                val translatedContent = when (selectedIndex) {
                    0 -> viewModel.optionATranslated.value ?: ""
                    1 -> viewModel.optionBTranslated.value ?: ""
                    2 -> viewModel.optionCTranslated.value ?: ""
                    else -> ""
                }

                val optionUserSelected = OptionUserSelected(
                    optionNumber = selectedIndex,
                    optionTitle = selectedOption.optionTitle,
                    optionContent = selectedOption.optionContent,
                    optionTranslated = translatedContent
                )
                detailViewModel.setSelectedOption(optionUserSelected)
            }
        }

        private fun updateOptionCardBackgrounds(selectedIndex: Int) {
            binding.optionACard.setBackgroundResource(
                if (selectedIndex == 0) R.drawable.option_selected_background else R.drawable.option_card_round
            )
            binding.optionBCard.setBackgroundResource(
                if (selectedIndex == 1) R.drawable.option_selected_background else R.drawable.option_card_round
            )
            binding.optionCCard.setBackgroundResource(
                if (selectedIndex == 2) R.drawable.option_selected_background else R.drawable.option_card_round
            )
        }

        companion object {
            private const val ARG_OPTION = "options"

            fun newInstance(options: List<FairyTaleOption>): FairyTaleSelectionFragment {
                val fragment = FairyTaleSelectionFragment()
                val args = Bundle().apply {
                    putParcelableArrayList(ARG_OPTION, ArrayList(options))
                }
                fragment.arguments = args
                return fragment
            }
        }
    }