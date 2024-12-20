package com.sangik.iluvbook.fairytale.detail.ui

import android.os.Bundle
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentFairyTaleDetailLastPageBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleLastPageViewModel

class FairyTaleDetailLastPageFragment : BaseFragment<FragmentFairyTaleDetailLastPageBinding, FairyTaleLastPageViewModel>(
    R.layout.fragment_fairy_tale_detail_last_page,
    FairyTaleLastPageViewModel::class
) {
    override fun initView() { setupUi() }

    private fun setupUi() {
        val fairyTaleTitle = arguments?.getString("fairyTaleTitle")
        viewModel.setTitle(fairyTaleTitle.toString())
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