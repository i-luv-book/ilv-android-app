package com.sangik.iluvbook.fairytale.detail.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleDetailBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel

class FairyTaleDetailFragment : Fragment() {
    private lateinit var vm : FairyTaleDetailViewModel
    private lateinit var binding : FragmentFairyTaleDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_detail, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this).get(FairyTaleDetailViewModel::class.java)
        binding.fairyTaleDetailViewModel = vm

        val fairyTaleArgs = FairyTaleDetailFragmentArgs.fromBundle(requireArguments())
        vm.setPages(fairyTaleArgs.fairyTaleResponse.pages)
        vm.setFairyTaleTitle(fairyTaleArgs.fairyTaleResponse.title)

        setupViewPager()
        initObserve()
        initListener()
    }

    private fun setupViewPager() {
        // adapter 연결
        val fairytaleAdapter = FairyTalePageAdapter(this, vm)
        binding.fairyTaleViewpager.adapter = fairytaleAdapter

        // 총 아이템 개수
        vm.setTotalItem(fairytaleAdapter.itemCount)

        // viewpager, indicator 연동
        binding.fairyTaleIndicator.setViewPager(binding.fairyTaleViewpager)

        // 페이지 변화에 따른 상태 업데이트
        binding.fairyTaleViewpager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vm.updateSwipeButtonState(position)
                vm.updateLastPageState(position)
                // CustomCircleIndicator 인디케이터 업데이트
                binding.fairyTaleIndicator.updateIndicatorState(position)
            }
        })
    }

    private fun initListener() {
        binding.apply {
            btnLeft.setOnClickListener { vm.swipeLeft(fairyTaleViewpager) }
            btnRight.setOnClickListener { vm.swipeRight(fairyTaleViewpager) }
        }
    }

    private fun initObserve() {
        // 왼쪽 버튼 상태 관리
        vm.isLeftButtonVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.apply {
                btnLeft.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                btnLeft.isClickable = isVisible
            }
        }

        // 오른쪽 버튼 상태 관리
        vm.isLastPage.observe(viewLifecycleOwner) { isLastPage ->
            binding.apply {
                btnRight.setBackgroundResource(if (isLastPage) R.drawable.btn_exit else R.drawable.btn_right)
                btnRight.setOnClickListener {
                    if (isLastPage) navigateToHomeFragment() else vm.swipeRight(fairyTaleViewpager)
                }
            }
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_fairyTaleDetailFragment_to_homeFragment)
    }
}