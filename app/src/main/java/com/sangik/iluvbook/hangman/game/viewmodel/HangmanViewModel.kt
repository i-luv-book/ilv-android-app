package com.sangik.iluvbook.hangman.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sangik.iluvbook.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class HangmanViewModel : BaseViewModel() {

    // 사용자가 클릭한 문자 저장
    private var clickedChar = mutableSetOf<Char>()
    // 힌트 저장
    private val _hint = MutableLiveData<String>()
    val hint : LiveData<String> get() = _hint

    // 키보드 표시될 문자 저장
    private val _keyboardChars = MutableLiveData<List<Char>>()
    val keyboardChars : LiveData<List<Char>> get() = _keyboardChars

    // 정답 단어 저장
    private val _answer = MutableLiveData<String>()
    val answer : LiveData<String> get() = _answer

    // 화면에 표시될 (사용자가 맞춘) 글자 저장
    private val _displayedAnswer = MutableLiveData<List<Char?>>()
    val displayedAnswer : LiveData<List<Char?>> get() = _displayedAnswer

    // 현재 행맨 레벨
    private val _hangmanLevel = MutableLiveData<Int>().apply { value = 0 }
    val hangmanLevel : LiveData<Int> get() = _hangmanLevel

    // 정답 맞춘지 여부
    private val _isClear = MutableLiveData<Boolean>().apply { value = false }
    val isClear : LiveData<Boolean> get() = _isClear


    // 사용자가 키보드 선택 시
    fun onKeyClicked(inputChar : Char) {
        clickedChar.add(inputChar)
        updateAnswerCard() // 사용자 input에 따른 정답 상태 업데이트
        isGameClear()
    }

    // 게임 정답 상태인지 확인
    private fun isGameClear(){
        if (displayedAnswer.value?.joinToString("") == answer.value ){
            _isClear.value = true
        }
    }

    // 사용자 입력에 따른 글자 업데이트
    private fun updateAnswerCard() {
        val answer = _answer.value ?: return

        // 표시된 정답 업데이트
        val updatedAnswer = _displayedAnswer.value?.toMutableList()
            ?: MutableList<Char?>(answer.length) { null }
        var isCorrect = false

        for (i in answer.indices) {
            if (answer[i] == clickedChar.last()) {  // 새로 추가된 글자만 비교
                updatedAnswer[i] = answer[i]
                isCorrect = true
            }
        }

        _displayedAnswer.value = updatedAnswer // 화면에 표시될 정답 상태 업데이트

        if (!isCorrect) {
            // 정답이 아닐 때 레벨 증가
            updateHangmanLevel()
        }
    }

    // 행맨 레벨 증가
    private fun updateHangmanLevel() {
        _hangmanLevel.value = (_hangmanLevel.value ?: 0) + 1
    }

    // 행맨 관련 데이터를 설정
    fun setHangmanData(answer: String, hint : String) {
        _answer.value = answer.uppercase()
        _displayedAnswer.value = List(answer.length) { null }
        _hint.value = hint
        _keyboardChars.value = generateKeyboardChars(answer)
    }


    private fun generateKeyboardChars(answer : String) : List<Char> {
        // 정답 문자열을 대문자로 변환, 중복 문자 제거
        val answerChars = answer.uppercase().toSet()

        // 필요한 키보드 문자 수 : 단어의 3배, 최대 26
        val keyboardLength = (answer.length * 3).coerceAtMost(26)

        // 키보드 표시 문자 집합 초기화
        val resultCharSet = answerChars.toMutableSet()

        // 남은 자리 채울 알파벳 (이미 포함된 문자 제거)
        val additionalChars = ('A'..'Z').toMutableList().apply { removeAll(resultCharSet) }

        // 키보드 문자 집합에 랜덤 문자 추가
        while (resultCharSet.size < keyboardLength && additionalChars.isNotEmpty()) {
            val randomChar = additionalChars.random()
            resultCharSet.add(randomChar)
            additionalChars.remove(randomChar)
        }
        return resultCharSet.toList().sorted() // 오름차순 정렬 반환
    }

}
