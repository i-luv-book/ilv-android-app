package com.sangik.iluvbook.fairytale.detail.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentFairyTaleDetailLastPageBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleLastPageViewModel
import com.sangik.iluvbook.fairytale.model.dto.Page

class FairyTaleDetailLastPageFragment : Fragment() {

    private lateinit var vm : FairyTaleLastPageViewModel
    private lateinit var binding : FragmentFairyTaleDetailLastPageBinding
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fairy_tale_detail_last_page, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(FairyTaleLastPageViewModel::class.java)
        binding.lastPageVm = vm
        setupUi()
    }

    private fun setupUi() {
        val fairyTaleTitle = arguments?.getString("fairyTaleTitle")
        vm.setTitle(fairyTaleTitle.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(fairyTaleTitle : String): FairyTaleDetailLastPageFragment {
            val fragment = FairyTaleDetailLastPageFragment()
            val args = Bundle()
            args.putString("fairyTaleTitle", fairyTaleTitle)
            fragment.arguments = args
            return fragment
        }
    }

}