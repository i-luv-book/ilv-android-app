package com.sangik.iluvbook.fairytalecreation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.ChipGroup
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleCreationBinding
import com.sangik.iluvbook.onboarding.viewmodel.OnboardingViewModel
import com.sangik.iluvbook.fairytalecreation.viewmodel.FairyTaleCreationViewModel
import com.sangik.iluvbook.hangman.intro.IntroHangmanFragment

class FairyTaleCreationFragment : Fragment() {

    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var vm: FairyTaleCreationViewModel
    private lateinit var binding : FragmentFairyTaleCreationBinding
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // OnboardingActivity와 공유하는 ViewModel
        onboardingViewModel = ViewModelProvider(requireActivity()).get(OnboardingViewModel::class.java)

        vm = ViewModelProvider(this).get(FairyTaleCreationViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_creation, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner // Live Data 사용을 위한 생명주기 소유자 설정
        binding.fairyTaleCreationViewModel = vm // 레이아웃의 ViewModel 속성에 현재 ViewModel을 할당

        initChipGroup() // chip Group 초기화
        initListener() // 리스너 초기화
        setupObserver() // Observer 설정 (난이도 설정 관련 텍스트)

        return binding.root
    }

    // Edit Text 외 빈 공간 클릭 시 입력 취소
    private fun initCancelClickListener() {
        setCancelClickListener(binding.frameWho, binding.addChipWhoFrame, binding.editTextWho, binding.chipGroupWho)
        setCancelClickListener(binding.frameName, binding.addChipNameFrame, binding.editTextName, binding.chipGroupName)
        setCancelClickListener(binding.cardViewWhere, binding.addChipWhereFrame, binding.editTextWhere, binding.chipGroupWhere)
        setCancelClickListener(binding.cardViewGenre, binding.addChipGenreFrame, binding.editTextGenre, binding.chipGroupGenre)
    }

    // 초기 Chip Group
    private fun initChipGroup() {
        setupChipGroup(binding.chipGroupWho, arrayOf("\uD83D\uDC64 사람", "\uD83D\uDC3E 동물", "\uD83D\uDC7D 외계인", "\uD83C\uDF2D 음식", "\uD83C\uDF2D 사물", "\uD83E\uDD95 공룡"), 1)
        setupChipGroup(binding.chipGroupName, arrayOf("지은", "진", "영규", "현아", "상익"), 2)
        setupChipGroup(binding.chipGroupWhere, arrayOf("\uD83C\uDFA1 놀이공원", "\uD83C\uDFEB 학교", "\uD83C\uDFD5\uFE0F 숲", "\uD83C\uDFD4\uFE0F 동굴", "\uD83C\uDFF0 왕국", "\uD83E\uDE90 우주"), 3)
        setupChipGroup(binding.chipGroupGenre, arrayOf("\uD83C\uDFD5 모험", "\uD83E\uDD84 판타지", "\uD83E\uDDB8\u200D♂\uFE0F 액션", "\uD83D\uDC80 호러", "\uD83E\uDDD9\u200D♂\uFE0F 마법", "\uD83C\uDFDB\uFE0F 과거"), 4)
    }

    // 난이도 설정 관련 텍스트 (추후 API 연결시 사용)
    private fun setupObserver() {
        onboardingViewModel.selectedLevel.observe(viewLifecycleOwner) { level ->
            binding.selectedLevel.text = level
        }
    }

    // 동화 생성 버튼 상태에 따른 변경
    private fun initCreateButtonListener() {
        vm.isAllGroupSelected.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnCreate.isClickable = isEnabled
            binding.btnCreate.setBackgroundResource(
                if (isEnabled) R.drawable.btn_create_on else R.drawable.btn_create_off
            )
        }
    }

    // 추가 버튼 클릭시 새로운 chip 추가
    private fun setAddButtonClickListener(
        frameLayout: FrameLayout,
        editText: EditText,
        addButton: ImageButton,
        chipGroup: ChipGroup,
        chipGroupNumber: Int
    ) {
        addButton.setOnClickListener {
            if (addButton.isClickable) {
                addUserInputChip(editText.text.toString(), chipGroup, chipGroupNumber)
                editText.text.clear()
                frameLayout.visibility = View.GONE
            }
        }
    }

    // 리스너 초기화
    private fun initListener() {
        initTextChangedListener()
        initAddButtonListener()
        initCancelClickListener()
        initCreateButtonListener()
        initCreateFairyTaleButtonListener()
    }

    // 행맨 게임 인트로 이동
    private fun initCreateFairyTaleButtonListener() {
        binding.btnCreate.setOnClickListener {
            navigateToHangmanFragment()
        }
    }

    // 행맨 인트로 이동
    private fun navigateToHangmanFragment(){
        val fragment = IntroHangmanFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.onboarding_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 추가 버튼 클릭 리스너
    private fun initAddButtonListener() {
        with(binding) {
            setAddButtonClickListener(addChipWhoFrame, editTextWho, btnAddWho, chipGroupWho, 1)
            setAddButtonClickListener(addChipNameFrame, editTextName, btnAddName, chipGroupName, 2)
            setAddButtonClickListener(addChipWhoFrame, editTextWhere, btnAddWhere, chipGroupWhere, 3)
            setAddButtonClickListener(addChipGenreFrame, editTextGenre, btnAddGenre, chipGroupGenre, 4)
        }
    }

    // 텍스트 변경 리스너 초기화
    private fun initTextChangedListener() {
        with(binding) {
            setTextChangedListener(editTextWho, chipGroupWho, 1)
            setTextChangedListener(editTextName, chipGroupName, 2)
            setTextChangedListener(editTextWhere, chipGroupWhere, 3)
            setTextChangedListener(editTextGenre, chipGroupGenre, 4)
        }
    }

    // 사용자 입력 필드 텍스트 변경 리스너 (vm에서 업데이트 및 유효성 검사 수행)
    private fun setTextChangedListener(editText: EditText, chipGroup: ChipGroup, chipGroupNumber: Int) {
        editText.addTextChangedListener { input ->
            vm.onInputChanged(input.toString(), chipGroup, chipGroupNumber)
        }
    }

    // 입력 중 다른 곳 클릭 시 뷰 제거, plus chip 재생성
    private fun setCancelClickListener(view : View, inputLayout:FrameLayout, editText:EditText, chipGroup: ChipGroup) {
        view.setOnClickListener {
            inputLayout.visibility = View.GONE
            editText.text.clear()
            createPlusChip(chipGroup, getFrameLayoutForChipGroup(chipGroup))
        }
    }

    // ChipGroup 초기화, 선택된 chip 업데이트, observer 설정
    private fun setupChipGroup(chipGroup: ChipGroup, initialChips: Array<String>, chipGroupNumber: Int) {
        initialChips.forEach{ chipText ->
            val customChip = createCustomChip(requireContext(), chipText)
            customChip.id = View.generateViewId() // 고유 ID를 부여
            chipGroup.addView(customChip)

            customChip.getChip().setOnCheckedChangeListener { _, isChecked ->
                // Chip 선택 여부를 ViewModel에 알려줌.
                vm.setChipSelectedState(customChip.id, chipGroupNumber)
            }
        }

        createPlusChip(chipGroup, getFrameLayoutForChipGroup(chipGroup))

        val selectedLiveData = when (chipGroupNumber) {
            1 -> vm.selectedChipGroup1
            2 -> vm.selectedChipGroup2
            3 -> vm.selectedChipGroup3
            4 -> vm.selectedChipGroup4
            else -> throw IllegalStateException("Invalid chipGroupNumber")
        }

        selectedLiveData.observe(viewLifecycleOwner) {selectedChipIds ->
            updateChipGroup(chipGroup, selectedChipIds)
        }
    }

    // chipGroup 내 Chip 상태 업데이트
    private fun updateChipGroup(chipGroup: ChipGroup, selectedChipIds: Set<Int>) {

        for (i in 0 until chipGroup.childCount) {
            val customChip = chipGroup.getChildAt(i) as? CustomChipView
            val chipId = chipGroup.getChildAt(i).id
            if (chipId != -1){
                customChip?.clearSelection()
            }
        }

        // 선택된 칩 상태
        selectedChipIds.forEachIndexed { index, id ->
            val customChip = chipGroup.findViewById<CustomChipView>(id)
            customChip?.setSelectedState(isFirst = index == 0)
        }
    }

    // custom Chip View 객체 생성
    private fun createCustomChip(context: Context, chipText: String): CustomChipView {
        return CustomChipView(context).apply { setChipText(chipText) }
    }

    // plus chip 생성 후 chipGroup에 추가
    private fun createPlusChip(chipGroup: ChipGroup, frameLayout : FrameLayout) {
        if (chipGroup.findViewWithTag<CustomChipView>("plusChip") != null){
            return
        }
        val plusChip = CustomChipView(requireContext()).apply {
            setChipText("+")
            getChip().isCheckable = false
            getChip().tag = "plusChip"
            getChip().setOnClickListener {
                frameLayout.visibility = View.VISIBLE
                chipGroup.removeView(this)
            }
        }
        chipGroup.addView(plusChip)
    }

    // 각 ChipGroup의 Frame Layout 반환
    private fun getFrameLayoutForChipGroup(chipGroup: ChipGroup): FrameLayout {
        return when (getChipGroupNumber(chipGroup)) {
            1 -> binding.addChipWhoFrame
            2 -> binding.addChipNameFrame
            3 -> binding.addChipWhereFrame
            4 -> binding.addChipGenreFrame
            else -> throw IllegalStateException("Invalid chipGroupNumber")
        }
    }

    // 사용자 입력 새로운 칩 추가
    private fun addUserInputChip(userInputText : String, chipGroup: ChipGroup, chipGroupNumber: Int) {
        if (userInputText.isBlank()) return

        val userChip = CustomChipView(requireContext()).apply {
            setChipText(userInputText)
            id = View.generateViewId()
            getChip().setOnClickListener {
                chipGroup.removeView(this) // 칩 다시 클릭 시 제거
                vm.setChipSelectedState(this.id, chipGroupNumber) // 상태 업데이트
            }
        }

        chipGroup.addView(userChip)
        // 새로운 칩 추가를 viewModel에 반영
        vm.setChipSelectedState(userChip.id, chipGroupNumber)
        // plus칩 다시 생성
        createPlusChip(chipGroup, getFrameLayoutForChipGroup(chipGroup))
    }

    // chip group 번호 반환
    private fun getChipGroupNumber(chipGroup : ChipGroup) : Int {
        return when (chipGroup) {
            binding.chipGroupWho -> 1
            binding.chipGroupName -> 2
            binding.chipGroupWhere -> 3
            binding.chipGroupGenre -> 4
            else -> throw IllegalStateException("Invaild")
        }
    }

}