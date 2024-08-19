package com.sangik.iluvbook.main.ui

import com.sangik.iluvbook.BR
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseActivity
import com.sangik.iluvbook.databinding.ActivityMainBinding
import com.sangik.iluvbook.util.ActivityUtil
import com.sangik.iluvbook.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    MainViewModel::class,
    BR.mainViewModel
) {

    override fun initListener() {
        super.initListener()

        binding.buttonShowToast.setOnClickListener {
            val activity = ActivityUtil()
            activity.startOnboardingActivity(this)
        }
    }
}