package com.sangik.iluvbook.fairytale.intro.ui

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentFairyTaleIntroBinding
import com.sangik.iluvbook.fairytale.intro.viewmodel.FairyTaleIntroViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class FairyTaleIntroFragment : BaseFragment<FragmentFairyTaleIntroBinding, FairyTaleIntroViewModel>(
    R.layout.fragment_fairy_tale_intro,
    FairyTaleIntroViewModel::class
) {
    private lateinit var introHangmanViewModel : IntroHangmanViewModel
    private val args : FairyTaleIntroFragmentArgs by navArgs()

    override fun initView() {
        introHangmanViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)

        // RecyclerView 설정
        setRecyclerView(args.keywords.toList())

        // 썸네일 이미지 설정
        setGlideImage()
    }

    override fun initObserver() {
        observeIntroHangmanViewModel()
    }

    override fun initListener() {
        actionToFairyTaleDetail()
    }


    private fun actionToFairyTaleDetail() {
        binding.btnReadFairytale.setOnClickListener {
            findNavController().navigate(R.id.action_fairyTaleIntroFragment_to_fairyTaleDetailFragment)
        }
    }

    private fun observeIntroHangmanViewModel() {
        // 동화 Response observe
        introHangmanViewModel.fairyTaleResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                viewModel.initData(response.title, response.summary, response.pages.first().imgURL)
            }
        }

        // 선택형 동화 Response observe
        introHangmanViewModel.fairyTaleSelectionResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                viewModel.initData(response.title, "", response.imgURL)
                binding.fairytaleIntroSummary.visibility = View.GONE
            }
        }
    }

    // 썸네일 이미지 설정
    private fun setGlideImage() {
        viewModel.fairyTaleImgUrl.observe(viewLifecycleOwner) {
            it.let {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.thumbnail)
            }
        }
    }

    // 키워드 설정
    private fun setRecyclerView(keywords : List<String>) {
        val adapter = KeywordAdapter(keywords)
        binding.keywordsRecyclerview.adapter = adapter
        binding.keywordsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
}
