package com.sangik.iluvbook.main.ui

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sangik.iluvbook.BR
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseActivity
import com.sangik.iluvbook.databinding.ActivityMainBinding
import com.sangik.iluvbook.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    MainViewModel::class,
    BR.mainViewModel
) {
    override fun onStart() {
        super.onStart()
        binding.bottomNavigation.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}