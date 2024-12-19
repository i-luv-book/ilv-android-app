package com.sangik.iluvbook.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.library.baseAdapters.BR
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

abstract class BaseFragment<T : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutResId: Int,
    private val viewModelClass: KClass<VM>,
) : Fragment() {

    lateinit var binding: T
    val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)

        initView()
        initObserver()
        initListeners()
        return binding.root
    }

    open fun initView() {}
    open fun initObserver() {}
    open fun initListeners() {}
}