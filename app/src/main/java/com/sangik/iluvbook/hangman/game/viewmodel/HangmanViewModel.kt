package com.sangik.iluvbook.hangman.game.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.network.service.HangmanService
import com.sangik.iluvbook.network.RetrofitClient
import com.sangik.iluvbook.network.repository.HangmanRepository
import kotlinx.coroutines.launch

class HangmanViewModel : BaseViewModel() {
    private val hangmanRepository : HangmanRepository

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

    private val _hangmanLevel = MutableLiveData<Int>()
    val hangmanLevel : LiveData<Int> get() = _hangmanLevel

    // 정답 맞춘지 여부

    private val _isClear = MutableLiveData<Boolean>()
    val isClear : LiveData<Boolean> get() = _isClear
    init {
        val apiService : HangmanService = RetrofitClient.createService(HangmanService::class.java)
        hangmanRepository = HangmanRepository(apiService)
        fetchWord() // 단어 호출
        clickedChar = mutableSetOf() // 문자 초기화
        _hangmanLevel.value = 0 // 초기 행맨 레벨
        _isClear.value = false // 초기 정답 상태
    }

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
        val updatedAnswer = _displayedAnswer.value?.toMutableList() ?: MutableList<Char?>(answer.length) { null }
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

    // 게임에서 사용할 단어
    private fun fetchWord() {
        setLoading(true) // 디자인 정해지면 로딩 처리
        viewModelScope.launch {
            try {
                val hangmanResponse = hangmanRepository.fetchWord()
                hangmanResponse?.let {
                    updateHint(it.hint)
                    updateAnswer(it.word)
                    _keyboardChars.value = processWord(it.word)
                }
            }catch (e : Exception) {
                Log.d("asdf", e.message.toString())
            }finally {
                setLoading(false)
            }
        }
    }

    private fun updateAnswer(answer : String) {
        _answer.value = answer.uppercase()
        _displayedAnswer.value = List(answer.length) { null }
    }

    private fun updateHint(hint : String) {
        _hint.value = hint
    }
    private fun processWord(answer : String) : List<Char> {
        val wordLength = answer.length
        val uniqueChars = answer.toCharArray().distinct() // 중복 제거된 문자
        val keyboardLength = (wordLength * 3).coerceAtMost(26) // 단어의 3배, 최대 26

        // 결과에 포함될 문자 집합
        val resultSet = mutableSetOf<Char>()
        resultSet.addAll(uniqueChars)

        // 남은 자리 채울 알파벳 (이미 포함된 문자 제거)
        val availableChars = ('A'..'Z').toMutableList().apply { removeAll(resultSet) }

        while (resultSet.size < keyboardLength) {
            resultSet.add(availableChars.random())
        }

        val resultSetUpperCase = resultSet.map { it.uppercaseChar() }.toMutableSet()
        return resultSetUpperCase.sorted() // 오름차순 정렬 반환
    }
}