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
    import com.sangik.iluvbook.R
    import com.sangik.iluvbook.databinding.FragmentHangmanBinding
    import com.sangik.iluvbook.hangman.game.viewmodel.HangmanViewModel

    class HangmanFragment : Fragment() {
        private lateinit var vm : HangmanViewModel
        private lateinit var binding : FragmentHangmanBinding
        private var isKeyboardInitialized = false

        override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            vm = ViewModelProvider(this).get(HangmanViewModel::class.java)
            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_hangman, container, false
            )

            binding.lifecycleOwner = viewLifecycleOwner
            binding.hangmanViewModel = vm

            setupKeyboard()
            observeLiveData()

            return binding.root
        }


        // 키보드에 표시될 문자 관찰
        private fun setupKeyboard() {
            vm.keyboardChars.observe(viewLifecycleOwner) { chars ->
                if (!isKeyboardInitialized) {
                    initKeyboard(chars)
                }
            }
        }

        // 키보드 초기화 및 설정
        private fun initKeyboard(chars : List<Char>) {
            binding.keyboardFlex.removeAllViews()

            chars.forEach { char ->
                val textView = LayoutInflater.from(context).inflate(
                    R.layout.hangman_keyboard,
                    binding.keyboardFlex, // 부모 그룹
                    false // 즉시 추가 안함
                )as TextView
                textView.text = char.toString()
                textView.setOnClickListener {
                    vm.onKeyClicked(char) // 글자 선택 했을 때 정의
                    textView.setBackgroundColor(Color.TRANSPARENT)
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.keyboard_selected_text_color))
                    textView.isClickable = false
                }
                binding.keyboardFlex.addView(textView)
            }
            isKeyboardInitialized = true
        }

        // 사용자 입력 칸 초기화. 단어 길이만큼 빈 칸 표시
        private fun initInput(answer : String) {
            for (i in 1..answer.length) {
                val textView = LayoutInflater.from(context).inflate(
                    R.layout.hangman_keyboard,
                    binding.inputWordFrame,
                    false
                )as TextView
                textView.setBackgroundResource(R.drawable.btn_hangman_input_answer)
                binding.inputWordFrame.addView(textView)
            }
        }


        // 게임 종료 UI 표시
        private fun showGameEndUi(isWin : Boolean) {
            if (isWin) {
                binding.gameStatus.setImageResource(R.drawable.hangman_game_great)
            }else {
                binding.gameStatus.setImageResource(R.drawable.hangman_game_lose)
            }

            binding.apply {
                keyboardFlex.visibility = View.GONE
                btnMoveFairytale.visibility = View.VISIBLE
                gameStatus.visibility = View.VISIBLE
            }
        }

        // LiveData 관찰
        private fun observeLiveData() {
            vm.apply {
                // 정답 업데이트, User input 초기화
                answer.observe(viewLifecycleOwner) { answer ->
                    initInput(answer)
                }

                // UI에 표시되는 맞춘 글자 업데이트
                displayedAnswer.observe(viewLifecycleOwner) { displayedAnswer ->
                    updateDisplayedAnswer(displayedAnswer)
                }

                // 모두 맞추었을 때 호출, 게임 종료 UI
                isClear.observe(viewLifecycleOwner) { isClear ->
                    if (isClear) { showGameEndUi(isClear) }
                }

                // 행맨 단계 변경시 호출, 이미지 업데이트
                // 마지막 단계일 경우 게임 종료 UI
                hangmanLevel.observe(viewLifecycleOwner) { level ->
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
            }
        }

        // 사용자가 맞춘 단어를 UI에 업데이트
        private fun updateDisplayedAnswer(displayedAnswer: List<Char?>) {

            // 자식 뷰 순회하며 정답 표시 갱신
            for (i in 0 until binding.inputWordFrame.childCount) {
                val textView = binding.inputWordFrame.getChildAt(i) as TextView
                textView.text = displayedAnswer.getOrNull(i)?.toString() ?: ""
            }

        }

    }

