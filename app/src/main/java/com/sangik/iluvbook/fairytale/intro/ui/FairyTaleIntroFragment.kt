package com.sangik.iluvbook.fairytale.intro.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleIntroBinding
import com.sangik.iluvbook.fairytale.intro.viewmodel.FairyTaleIntroViewModel

class FairyTaleIntroFragment : Fragment() {
    private lateinit var vm: FairyTaleIntroViewModel
    private lateinit var binding: FragmentFairyTaleIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_intro, container, false
        )

        // ViewModel 초기화
        vm = ViewModelProvider(this).get(FairyTaleIntroViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.fairyTaleIntroViewModel = vm

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fairyTaleResponseArgs = FairyTaleIntroFragmentArgs.fromBundle(requireArguments())
        vm.initData(fairyTaleResponseArgs.fairyTaleResponse, fairyTaleResponseArgs.keywords.toList())

        // RecyclerView 설정
        setRecyclerView(fairyTaleResponseArgs.keywords.toList())

        setGlideImage()
    }

    private fun setGlideImage() {
        vm.fairyTaleResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Glide.with(this)
                    .load(it.pages.firstOrNull()?.imgURL ?: "")
                    .centerCrop()
                    .into(binding.thumbnail)
            }
        }
    }

    private fun setRecyclerView(keywords : List<String>) {
        val adapter = KeywordAdapter(keywords)
        binding.keywordsRecyclerview.adapter = adapter
        binding.keywordsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
}
