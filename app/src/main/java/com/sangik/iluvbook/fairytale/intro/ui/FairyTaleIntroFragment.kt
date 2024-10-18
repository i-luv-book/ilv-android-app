package com.sangik.iluvbook.fairytale.intro.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleIntroBinding
import com.sangik.iluvbook.fairytale.intro.viewmodel.FairyTaleIntroViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel

class FairyTaleIntroFragment : Fragment() {
    private lateinit var vm: FairyTaleIntroViewModel
    private lateinit var introHangmanViewModel : IntroHangmanViewModel
    private lateinit var binding: FragmentFairyTaleIntroBinding
    private val args : FairyTaleIntroFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_intro, container, false
        )

        // ViewModel 초기화
        vm = ViewModelProvider(this).get(FairyTaleIntroViewModel::class.java)
        introHangmanViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.fairyTaleIntroViewModel = vm

        observeIntroHangmanViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        setRecyclerView(args.keywords.toList())

        // 썸네일 이미지 설정
        setGlideImage()

        initListener()
    }

    private fun initListener() {
        // 동화 상세로 navigate
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
                vm.initData(response.title, response.summary, response.pages.first().imgURL)
            }
        }

        // 선택형 동화 Response observe
        introHangmanViewModel.fairyTaleSelectionResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                vm.initData(response.title, "", response.imgURL)
                binding.fairytaleIntroSummary.visibility = View.GONE
            }
        }
    }

    // 썸네일 이미지 설정
    private fun setGlideImage() {
        vm.fairyTaleImgUrl.observe(viewLifecycleOwner) {
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
