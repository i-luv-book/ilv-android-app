package com.sangik.iluvbook.hangman.game.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sangik.iluvbook.R
import com.sangik.iluvbook.databinding.FragmentHangmanBinding
import com.sangik.iluvbook.hangman.game.viewmodel.HangmanViewModel
import com.sangik.iluvbook.hangman.intro.viewmodel.IntroHangmanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HangmanFragment : Fragment() {
    private lateinit var hangmanViewModel: HangmanViewModel
    private lateinit var introViewModel: IntroHangmanViewModel
    private lateinit var binding: FragmentHangmanBinding
    private val args: HangmanFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hangmanViewModel = ViewModelProvider(this).get(HangmanViewModel::class.java)
        introViewModel = ViewModelProvider(requireActivity()).get(IntroHangmanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hangman, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.hangmanViewModel = hangmanViewModel

        setupKeyboard()
        observeHangmanViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeIntroViewModel()
        initListener()
    }


    private fun initListener() {
        binding.btnMoveFairytale.setOnClickListener {
            navigateToFairyTaleDetail()
        }
    }

    // 동화 상세로 이동
    private fun navigateToFairyTaleDetail() {
        val fairyTaleResponse = introViewModel.fairyTaleResponse.value
        if (fairyTaleResponse != null) {
            val actionToFairyTaleIntro = HangmanFragmentDirections
                .actionHangmanFragmentToFairyTaleIntroFragment(fairyTaleResponse, args.keywords)
            findNavController().navigate(actionToFairyTaleIntro)
        }
    }

    private fun navigateToFairyTaleLoading() {
        val actionToLoading = HangmanFragmentDirections
            .actionHangmanFragmentToFairyTaleLoadingFragment(args.keywords)
        findNavController().navigate(actionToLoading)
    }

    private fun observeIntroViewModel() {
        introViewModel.apply {
            hangmanResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    hangmanViewModel.setHangmanData(response.word, response.hint) // 행맨 데이터 세팅
                    initInput(response.word) // 사용자 입력칸 생성
                }
            }

            fairyTaleResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    binding.btnMoveFairytale.let {
                        it.setBackgroundResource(R.drawable.btn_move_fairytale)
                        it.isClickable = true
                    }
                }
            }
        }
    }

    // LiveData 관찰
    private fun observeHangmanViewModel() {
        hangmanViewModel.apply {
            answer.observe(viewLifecycleOwner) { answer ->
                initInput(answer)
            }
            // 답안
            displayedAnswer.observe(viewLifecycleOwner) { displayedAnswer ->
                updateDisplayedAnswer(displayedAnswer)
            }
            // 게임 성공 여부
            isClear.observe(viewLifecycleOwner) { isClear ->
                if (isClear) {
                    showGameEndUi(isClear)
                }
            }
            // 게임 완료 시 2초 후 FairyTaleLoading으로 이동
            isGameEnd.observe(viewLifecycleOwner) { isGameEnd ->
                if (isGameEnd) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        navigateToFairyTaleLoading()
                    }
                }
            }
            // 행맨 단계 변화
            hangmanLevel.observe(viewLifecycleOwner) { level ->
                updateHangmanStatus(level)
            }
        }
    }

    // 오답에 따른 행맨 UI 변경
    private fun updateHangmanStatus(level: Int?) {
        binding.hangman.setImageResource(
            when (level) {
                0 -> R.drawable.hm_0
                1 -> R.drawable.hm_1
                2 -> R.drawable.hm_2
                3 -> R.drawable.hm_3
                4 -> R.drawable.hm_4
                5 -> R.drawable.hm_5
                6 -> R.drawable.hm_6
                else -> R.drawable.hm_0
            }
        )
        if (level == 6) {
            showGameEndUi(false)
        }
    }

    // 키보드에 표시될 문자 관찰
    private fun setupKeyboard() {
        hangmanViewModel.keyboardChars.observe(viewLifecycleOwner) { chars ->
                initKeyboard(chars)
        }
    }

    // 키보드 초기화 및 설정
    private fun initKeyboard(chars: List<Char>) {
        binding.keyboardFlex.removeAllViews()
        val inflater = LayoutInflater.from(context)
        chars.forEach { char ->
            val keyCapText = inflater.inflate(R.layout.hangman_keyboard,
                binding.keyboardFlex, // 부모 그룹
                false // 즉시 추가 안함
            ) as TextView

            keyCapText.text = char.toString()
            keyCapText.setOnClickListener {
                hangmanViewModel.onKeyClicked(char) // 글자 선택 했을 때 정의
                keyCapText.apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.keyboard_selected_text_color
                        )
                    )
                    isClickable = false
                }
            }
            binding.keyboardFlex.addView(keyCapText)
        }
    }

    // 사용자 입력 칸 초기화. 단어 길이만큼 빈 칸 표시
    private fun initInput(answer: String) {
        binding.inputWordFrame.removeAllViews()  // 기존 뷰 제거
        val inflater = LayoutInflater.from(context)

        repeat(answer.length) {
            val userInputTextView = inflater.inflate(
                R.layout.hangman_keyboard,
                binding.inputWordFrame,
                false
            ) as TextView
            userInputTextView.setBackgroundResource(R.drawable.btn_hangman_input_answer)
            binding.inputWordFrame.addView(userInputTextView)
        }
    }


    // 게임 종료 UI 표시
    private fun showGameEndUi(isWin: Boolean) {
        binding.gameStatus.setImageResource(
            if (isWin) R.drawable.hangman_game_great else R.drawable.hangman_game_lose
        )

        binding.apply {
            keyboardFlex.visibility = View.GONE
            btnMoveFairytale.visibility = View.VISIBLE
            gameStatus.visibility = View.VISIBLE
        }
    }

    // 사용자가 맞춘 단어를 UI에 업데이트
    private fun updateDisplayedAnswer(displayedAnswer: List<Char?>) {
        // 자식 뷰 순회하며 정답 표시 갱신
        for (i in displayedAnswer.indices) {
            val textViewUserHit = binding.inputWordFrame.getChildAt(i) as TextView
            textViewUserHit.text = displayedAnswer.getOrNull(i)?.toString() ?: ""
        }
    }
}
