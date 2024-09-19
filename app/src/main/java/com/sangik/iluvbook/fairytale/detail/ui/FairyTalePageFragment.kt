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
import com.sangik.iluvbook.databinding.FragmentFairyTalePageBinding
import com.sangik.iluvbook.fairytale.detail.viewmodel.FairyTalePageViewModel
import com.sangik.iluvbook.fairytale.model.dto.Page
import java.util.Locale

class FairyTalePageFragment : Fragment() {
    private lateinit var binding : FragmentFairyTalePageBinding
    private lateinit var vm : FairyTalePageViewModel
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fairy_tale_page, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(FairyTalePageViewModel::class.java)
        binding.pageViewModel = vm

        setupUi()
        initObserver()
        vm.initTranslator()
    }

    // UI 및 데이터 설정
    private fun setupUi() {
        arguments?.getParcelable<Page>("pageData")?.let { pageData ->
            Glide.with(this).load(pageData.imgURL).into(binding.pageImage)
            vm.setOriginalContent(pageData.content) // 원본 동화 (영어)
        }

        arguments?.getString("fairyTaleTitle")?.let { vm.setFairyTaleTitle(it) }
    }

    // ViewModel의 LiveData 옵저버 설정
    private fun initObserver() {
        // TTS 버튼 활성화 관리
        vm.isTTSButtonActive.observe(viewLifecycleOwner) { isActive ->
            updateButtonState(binding.btnTts, isActive, R.drawable.btn_tts_active, R.drawable.btn_tts)
            handleTTS(isActive)
        }

        // 번역 버튼 활성화 관리
        vm.isTranslationButtonActive.observe(viewLifecycleOwner) { isActive ->
            updateButtonState(binding.btnTranslate, isActive, R.drawable.btn_translation_active, R.drawable.btn_translation)
            if (isActive) vm.translateFairyTale() else vm.setOriginalFairyTale()
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
                val language = if (vm.isTranslationButtonActive.value == true) Locale.KOREA else Locale.ENGLISH
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
        vm.deactivateAllStates() // ViewPager 스와이프 화면 이동 시 상태 비활성화
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    companion object {
        @JvmStatic
        fun newInstance(pageData: Page?, fairyTaleTitle : String): FairyTalePageFragment {
            val fragment = FairyTalePageFragment()
            val args = Bundle()
            args.putString("fairyTaleTitle", fairyTaleTitle)
            args.putParcelable("pageData", pageData)
            fragment.arguments = args
            return fragment
        }
    }
}