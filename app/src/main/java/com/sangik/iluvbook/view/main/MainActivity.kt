package com.sangik.iluvbook.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sangik.iluvbook.BR
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseActivity
import com.sangik.iluvbook.databinding.ActivityMainBinding
import com.sangik.iluvbook.util.ActivityUtil

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