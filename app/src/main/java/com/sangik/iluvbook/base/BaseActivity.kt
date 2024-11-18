package com.sangik.iluvbook.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutResId : Int,
    private val viewModelClass: KClass<VM>,
    private val viewModelId : Int
) : AppCompatActivity(){

    val binding : T by lazy {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    protected val viewModel : VM by lazy {
        ViewModelProvider(this).get(viewModelClass.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        initDataBinding()
        initListener()
    }

    open fun initDataBinding() {
        binding.also {
            if (viewModelId > 0) {
                it.setVariable(viewModelId, viewModel)
            }
            it.lifecycleOwner = this@BaseActivity
        }
    }

    open fun init(savedInstanceState: Bundle?) {}

    open fun initListener() {}

    private fun observeViewModel() {}
}