    package com.sangik.iluvbook.fairytale.detail.ui

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.databinding.DataBindingUtil
    import androidx.lifecycle.ViewModelProvider
    import com.sangik.iluvbook.R
    import com.sangik.iluvbook.databinding.FragmentFairyTaleSelectionBinding
    import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
    import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleSelectionViewModel
    import com.sangik.iluvbook.fairytale.model.dto.FairyTaleOption

    class FairyTaleSelectionFragment : Fragment() {
        private lateinit var selectionViewModel : FairyTaleSelectionViewModel
        private lateinit var detailViewModel : FairyTaleDetailViewModel
        private lateinit var binding : FragmentFairyTaleSelectionBinding
        private lateinit var options: List<FairyTaleOption>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            options = arguments?.getParcelableArrayList(ARG_OPTION) ?: emptyList()
            detailViewModel = ViewModelProvider(requireActivity()).get(FairyTaleDetailViewModel::class.java)
            selectionViewModel = ViewModelProvider(requireActivity()).get(FairyTaleSelectionViewModel::class.java)
            selectionViewModel.setOptions(options)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_fairy_tale_selection, container, false
            )

            binding.selectionViewModel = selectionViewModel

            binding.lifecycleOwner = viewLifecycleOwner

            observeSelectionViewModel()
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            initListener()
        }

        private fun observeSelectionViewModel() {
            // 옵션 카드 상태 변경
            selectionViewModel.selectedOptionIndex.observe(viewLifecycleOwner) { selectedIndex ->
                updateOptionCardBackgrounds(selectedIndex)
            }

            // 사용자 선택 후 새 동화 관련 Response Observing
            selectionViewModel.fairyTaleSelectionResponseList.observe(viewLifecycleOwner) { response ->
                response?.let {
                    if (it.isNotEmpty()) {
                        selectionViewModel.setupNewSelectionOption(it.last())
                    }
                }
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

        private fun initListener() {
            binding.apply {
                optionACard.setOnClickListener {
                    selectionViewModel?.toggleOptionSelection(0)
                }
                optionBCard.setOnClickListener {
                    selectionViewModel?.toggleOptionSelection(1)
                }
                optionCCard.setOnClickListener {
                    selectionViewModel?.toggleOptionSelection(2)
                }
            }
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