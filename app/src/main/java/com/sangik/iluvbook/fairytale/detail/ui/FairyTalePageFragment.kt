package com.sangik.iluvbook.fairytale.detail.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sangik.iluvbook.R
import com.sangik.iluvbook.base.BaseFragment
import com.sangik.iluvbook.databinding.FragmentFairyTalePageBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTaleDetailViewModel
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTalePageViewModel
import java.util.Locale

class FairyTalePageFragment : BaseFragment<FragmentFairyTalePageBinding, FairyTalePageViewModel>(
    R.layout.fragment_fairy_tale_page,
    FairyTalePageViewModel::class
) {
    private lateinit var detailViewModel : FairyTaleDetailViewModel
    private var tts: TextToSpeech? = null

    override fun initView() {
        detailViewModel = ViewModelProvider(requireActivity()).get(FairyTaleDetailViewModel::class.java)
        binding.detailViewModel = detailViewModel

        setupUi()
        viewModel.initTranslator()
    }

    // UI 및 데이터 설정
    private fun setupUi() {
        val fairyTaleTitle = arguments?.getString("fairyTaleTitle") ?: "Fairytale title"
        val fairyTaleContent = arguments?.getString("fairyTaleContent") ?: "Fairytale Content"
        val imgUrl = arguments?.getString("fairyTaleImgUrl")

        viewModel.setFairyTaleTitle(fairyTaleTitle)
        viewModel.setOriginalContent(fairyTaleContent)
        Glide.with(this).load(imgUrl).into(binding.pageImage)
    }

    // ViewModel의 LiveData 옵저버 설정
    override fun initObserver() {
        // TTS 버튼 활성화 관리
        viewModel.isTTSButtonActive.observe(viewLifecycleOwner) { isActive ->
            updateButtonState(binding.btnTts, isActive, R.drawable.btn_tts_active, R.drawable.btn_tts)
            handleTTS(isActive)
        }

        // 번역 버튼 활성화 관리
        viewModel.isTranslationButtonActive.observe(viewLifecycleOwner) { isActive ->
            updateButtonState(binding.btnTranslate, isActive, R.drawable.btn_translation_active, R.drawable.btn_translation)
            if (isActive) viewModel.translateFairyTale() else viewModel.setOriginalFairyTale()
        }
    }

    // TTS 행동 관리
    private fun handleTTS(isActive : Boolean) {
        if (isActive) startTTS() else stopTTS()
    }

    // TTS 기능 시작
    private fun startTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // 번역 버튼 상태에 따라 언어 설정
                val language = if (viewModel.isTranslationButtonActive.value == true) Locale.KOREA else Locale.ENGLISH
                tts?.language = language
                tts?.speak(binding.pageContent.text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    // TTS 기능을 중지
    private fun stopTTS() {
        tts?.stop()
    }

    // 버튼 상태를 업데이트
    private fun updateButtonState(btnLanguageAssist: View, isActive: Boolean, activeRes: Int, inactiveRes: Int) {
        btnLanguageAssist.setBackgroundResource(if (isActive) activeRes else inactiveRes)
    }

    override fun onPause() {
        super.onPause()
        viewModel.deactivateAllStates() // ViewPager 스와이프 화면 이동 시 상태 비활성화
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            fairyTaleTitle : String,
            fairyTaleContent: String,
            imgUrl : String,
        ): FairyTalePageFragment {
            val fragment = FairyTalePageFragment()
            val args = Bundle().apply {
                putString("fairyTaleTitle", fairyTaleTitle)
                putString("fairyTaleContent", fairyTaleContent)
                putString("fairyTaleImgUrl", imgUrl)
            }
            fragment.arguments = args
            return fragment
        }
    }
}