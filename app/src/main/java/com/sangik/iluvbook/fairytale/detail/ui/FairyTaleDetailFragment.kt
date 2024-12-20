package com.sangik.iluvbook.fairytale.detail.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentFairyTaleDetailBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleSelectionViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class FairyTaleDetailFragment :
    BaseFragment<FragmentFairyTaleDetailBinding, FairyTaleDetailViewModel>(
        R.layout.fragment_fairy_tale_detail,
        FairyTaleDetailViewModel::class
    ) {
    private lateinit var introViewModel: IntroHangmanViewModel
    private lateinit var selectionViewModel: FairyTaleSelectionViewModel

    override fun initView() {
        // Activity 범위 ViewModel
        introViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
        selectionViewModel = ViewModelProvider(requireActivity()).get(FairyTaleSelectionViewModel::class.java)
    }

    override fun initListener() {
        binding.btnLeft.setOnClickListener {
            viewModel.swipeLeft(binding.fairyTaleViewpager)
        }
        binding.btnRight.setOnClickListener {
            when {
                // 마지막 페이지인 경우
                viewModel.isLastPage.value == true -> {
                    navigateToHomeFragment()
                }
                // 옵션 선택 페이지인 경우
                viewModel.isSelectionPage.value == true -> {
                    callSelectionFairyTale()
                    // 옵션 선택 후 비활성화 상태 설정
                    disableSelectionButton()
                }
                else -> {
                    viewModel.swipeRight(binding.fairyTaleViewpager)
                }
            }
        }
    }

    override fun initObserver() {
        observeIntroViewModel()
        observeDetailViewModel()
        observeSelectionViewModel()
    }

    // 일반형 동화 및 선택형 동화 응답 처리
    private fun observeIntroViewModel() {
        introViewModel.apply {
            // 일반형 동화 응답 관찰
            fairyTaleResponse.observe(viewLifecycleOwner) { response ->
                viewModel.addFairyTaleResponse(response)
                updateIndicator() // 인디케이터 상태 업데이트

                // 일반 동화가 로드될 때 좌우 버튼 상태
                viewModel.updateSwipeButtonState(viewModel.getCurrentPageIndex())
            }

            // 선택형 동화 응답 관찰
            fairyTaleSelectionResponse.observe(viewLifecycleOwner) { response ->
                viewModel.addFairyTaleSelectionResponse(response)
                updateIndicator()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        // CustomCircleIndicator와 ViewModel 연동
        binding.fairyTaleIndicator.bindViewModel(viewModel)
    }

    private fun setupViewPager() {
        // adapter 연결
        val fairytaleAdapter = FairyTalePageAdapter(this, viewModel)
        binding.fairyTaleViewpager.adapter = fairytaleAdapter

        // 초기 페이지 설정
        binding.fairyTaleViewpager.setCurrentItem(0, false)

        // 초기 상태 업데이트
        viewModel.updateSwipeButtonState(0)

        // viewpager, indicator 연동
        binding.fairyTaleIndicator.setViewPager(binding.fairyTaleViewpager)


        // 페이지 변화에 따른 상태 업데이트
        binding.fairyTaleViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // ViewModel의 현재 페이지 인덱스를 업데이트
                viewModel.setCurrentPageIndex(position)

                // 페이지 이동 버튼 상태 업데이트
                viewModel.updateSwipeButtonState(position)

                // CustomCircleIndicator 인디케이터 업데이트
                binding.fairyTaleIndicator.updateIndicatorState(viewModel, position)
            }
        })
    }

    // 옵션 선택 후 비활성화 상태 설정
    private fun disableSelectionButton() {
        binding.btnRight.setBackgroundResource(R.drawable.btn_select_inactive)
        binding.btnRight.isClickable = false
    }

    // 선택형 동화 호출
    private fun callSelectionFairyTale() {
        val keywords = introViewModel.keywords.value ?: return
        val currentContent = viewModel.createNewContent()
        val currentIndex = viewModel.getCurrentPageIndex()

        if (currentIndex == 3) { // 마지막 선택형 동화 호출
            selectionViewModel.callLastSelectionFairyTale(keywords, currentContent)
        } else { // 선택형 동화 호출
            selectionViewModel.callNewSelectionFairyTale(keywords, currentContent)
        }
    }

    private fun observeSelectionViewModel() {
        // 옵션 선택 여부에 따른 선택 버튼 상태 업데이트
        selectionViewModel.selectedOptionIndex.observe(viewLifecycleOwner) { selectedIndex ->
            updateSelectionButtonState(selectedIndex)
        }

        // 로딩 상태 UI 관리
        selectionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            updateLoadingState(isLoading)
        }

        // 동화 List
        selectionViewModel.fairyTaleSelectionResponseList.observe(viewLifecycleOwner) { responseList ->
            responseList?.lastOrNull()?.let { response ->
                viewModel.addFairyTaleSelectionResponse(response)
                binding.fairyTaleViewpager.adapter?.notifyDataSetChanged()
                updateIndicators()

                viewModel.updateSwipeButtonState(viewModel.getCurrentPageIndex())
            }
        }

        // 마지막 동화 호출 response observing
        selectionViewModel.fairyTaleLastResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                viewModel.addLastFairyTaleResponse(response)
                binding.fairyTaleViewpager.adapter?.notifyDataSetChanged()
                updateIndicators()
            }
        }
    }

    // Indicator 상태 업데이트
    private fun updateIndicators() {
        val indicatorCount = viewModel.getTotalPageCount()
        binding.fairyTaleIndicator.createIndicators(indicatorCount, viewModel.getCurrentPageIndex())
        binding.fairyTaleIndicator.updateIndicatorState(viewModel, viewModel.getCurrentPageIndex())
    }

    // 옵션 선택 버튼 상태 업데이트
    private fun updateSelectionButtonState(selectedIndex: Int) {
        binding.btnRight.apply {
            isClickable = selectedIndex != -1
            setBackgroundResource(if (selectedIndex != -1) R.drawable.btn_select else R.drawable.btn_select_inactive)
        }
    }

    // 로딩 상태 업데이트
    private fun updateLoadingState(isLoading: Boolean) {
        binding.apply {
            fairyTaleLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnRight.isClickable = !isLoading
            btnLeft.visibility = if (isLoading) View.GONE else View.VISIBLE
            fairyTaleViewpager.isUserInputEnabled = !isLoading
        }
    }

    // 페이지 특성에 따른 상태 관리를 위한 observing
    private fun observeDetailViewModel() {

        // 일반 동화 버튼 상태 관리
        viewModel.isGeneralPage.observe(viewLifecycleOwner) {
            updateButtonState()
        }

        // 선택형 동화 버튼 상태 관리
        viewModel.isSelectionPage.observe(viewLifecycleOwner) {
            updateButtonState()
        }

        // 왼쪽 버튼 상태 관리
        viewModel.isLeftButtonVisible.observe(viewLifecycleOwner) {
            updateButtonState()
        }

        // 마지막 페이지 오른쪽 버튼 상태 관리
        viewModel.isLastPage.observe(viewLifecycleOwner) {
            updateButtonState()
        }

    }

    // 버튼 상태 업데이트
    private fun updateButtonState() {
        when {
            // 마지막 페이지인 경우
            viewModel.isLastPage.value == true -> {
                rightButtonToExitDrawable()
            }

            viewModel.isSelectionPage.value == true -> {
                // 옵션 선택 비활성화 버튼으로 바꾸기
                rightButtonToSelectInactiveDrawable()
                hideLeftButton()
            }

            else -> {
                setBasicRightButtonDrawable()
            }
        }

        // 왼쪽 버튼 상태는 첫 페이지인지 여부로 결정
        binding.btnLeft.visibility =
            if (viewModel.isLeftButtonVisible.value == true) View.VISIBLE else View.INVISIBLE
        binding.btnLeft.isClickable = viewModel.isLeftButtonVisible.value == true
    }

    // 옵션 선택 버튼 (비활성화)로 변경
    private fun rightButtonToSelectInactiveDrawable() {
        binding.btnRight.apply {
            setBackgroundResource(R.drawable.btn_select_inactive)
            isClickable = false
        }
    }

    // 오른쪽 버튼을 나가기 버튼으로 변경
    private fun rightButtonToExitDrawable() {
        binding.btnRight.setBackgroundResource(R.drawable.btn_exit)
    }

    // 기본 오른쪽 버튼
    private fun setBasicRightButtonDrawable() {
        binding.btnRight.apply {
            setBackgroundResource(R.drawable.btn_right)
            isClickable = true
        }
    }

    // 왼쪽 버튼 가리기
    private fun hideLeftButton() {
        binding.btnLeft.visibility = View.INVISIBLE
        binding.btnLeft.isClickable = false
    }

    // Indicator 상태 업데이트
    private fun updateIndicator() {
        val indicatorCount = viewModel.getTotalPageCount()
        binding.fairyTaleIndicator.createIndicators(indicatorCount, viewModel.getCurrentPageIndex())
    }

    // 메인 화면으로 이동
    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_fairyTaleDetailFragment_to_homeFragment)
    }
}