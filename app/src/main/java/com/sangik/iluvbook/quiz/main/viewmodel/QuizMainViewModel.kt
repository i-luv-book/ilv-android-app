package com.sangik.iluvbook.quiz.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sangik.iluvbook.base.BaseViewModel
import com.sangik.iluvbook.network.repository.QuizMainRepository
import com.sangik.iluvbook.network.repository.UserPreferenceRepository
import com.sangik.iluvbook.network.repository.UserProfileRepository
import com.sangik.iluvbook.quiz.main.model.UserProfileData
import com.sangik.iluvbook.quiz.main.model.QuizStatsData
import com.sangik.iluvbook.quiz.main.model.QuizTotalItem
import kotlinx.coroutines.launch

class QuizMainViewModel(
    private val userPreferencesRepository: UserPreferenceRepository,
    private val quizMainRepository: QuizMainRepository,
    private val userProfileRepository: UserProfileRepository
) : BaseViewModel(){

    // 사용자 프로필 정보
    private val _userProfile = MutableLiveData<UserProfileData>()
    val userProfile : LiveData<UserProfileData> get() = _userProfile

    // 총 퀴즈 점수 통계
    private val _quizStatsData = MutableLiveData<QuizStatsData>()
    val quizStatsData : LiveData<QuizStatsData> get() = _quizStatsData

    // 전체 퀴즈 목록
    private val _quizTotalItems = MutableLiveData<List<QuizTotalItem>>()
    val quizTotalItems : LiveData<List<QuizTotalItem>> get() = _quizTotalItems


    // 상태 flag
    private var isLoadingItems = false
    var isLastPage = false
    private var lastFairyTaleId: Long = 0L // 초기값 : 0

    private var nickname: String
    init {
        nickname = userPreferencesRepository.getNickname()
        loadUserProfile() // 유저 프로필 조회
        loadQuizItems() // 전체 퀴즈 목록 조회
        fetchQuizStats() // 퀴즈 통계 조회
    }

    // 유저 프로필 조회
    private fun loadUserProfile() {
        viewModelScope.launch {
            setLoading(true)
            val result = userProfileRepository.getProfileData(nickname)
            result.fold(
                onSuccess = { profile ->
                    _userProfile.value = profile
                },
                onFailure = { error ->
                    errorMessage.value = error.message
                }
            )
            setLoading(false)
        }
    }

    // 퀴즈 통계 조회
    private fun fetchQuizStats() {
        viewModelScope.launch {
            setLoading(true)
            val result = quizMainRepository.getQuizStat(nickname)
            result.fold(
                onSuccess = { stats ->
                    _quizStatsData.value = stats
                },
                onFailure = { error ->
                    errorMessage.value = error.message
                }
            )
            setLoading(false)
        }
    }

    // 전체 퀴즈 목록 조회
    fun loadQuizItems() {
        if (isLoadingItems || isLastPage) return // 미지막 페이지 확인
        isLoadingItems = true // 중복 로딩 방지

        viewModelScope.launch {
            val result = quizMainRepository.getQuizItems(nickname, lastFairyTaleId)
            result.fold(
                onSuccess = { newItems ->
                    // 빈 리스트가 반환 : 마지막 페이지
                    if (newItems.isEmpty()) {
                        isLastPage = true
                    } else {
                        lastFairyTaleId = newItems.last().fairyTaleId // 가장 마지막 동화 id 업데이트
                        _quizTotalItems.value = _quizTotalItems.value.orEmpty() + newItems
                    }
                },

                onFailure = { error ->
                    errorMessage.value = error.message ?: "데이터를 불러오는데 실패했습니다."
                }
            )
            isLoadingItems = false
        }
    }
}