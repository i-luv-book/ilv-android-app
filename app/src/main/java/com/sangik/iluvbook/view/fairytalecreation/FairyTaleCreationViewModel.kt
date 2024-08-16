package com.sangik.iluvbook.view.fairytalecreation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.ChipGroup
import com.sangik.iluvbook.base.BaseViewModel

class FairyTaleCreationViewModel : BaseViewModel(){

    // 선택된 Chip Id 저장
    private val selectedChipGroups = List(4) {MutableLiveData<Set<Int>>(emptySet()) }

    // 각 칩 그룹 LiveData (읽기)
    val selectedChipGroup1: LiveData<Set<Int>> get() = selectedChipGroups[0]
    val selectedChipGroup2: LiveData<Set<Int>> get() = selectedChipGroups[1]
    val selectedChipGroup3: LiveData<Set<Int>> get() = selectedChipGroups[2]
    val selectedChipGroup4: LiveData<Set<Int>> get() = selectedChipGroups[3]

    // 모든 그룹 선택 여부
    val isAllGroupSelected = MutableLiveData<Boolean>().apply { value = false }

    // 각 입력 필드 유효성
    val isWhoInputValid = MutableLiveData(false)
    val isNameInputValid = MutableLiveData(false)
    val isWhereInputValid = MutableLiveData(false)
    val isGenreInputValid = MutableLiveData(false)

    // 각 입력 필드 LiveData
    val whoInput = MutableLiveData<String>()
    val nameInput = MutableLiveData<String>()
    val whereInput = MutableLiveData<String>()
    val genreInput = MutableLiveData<String>()

    // 칩 그룹 전체 설정 여부
    init {
        selectedChipGroups.forEach {it.observeForever{updateAllGroupSelectedState()}}
    }

    // 사용자 입력 칩 입력 필드 변경시
    fun onInputChanged(input: String, chipGroup: ChipGroup, chipGroupNumber: Int) {
        when (chipGroupNumber) {
            1 -> updateInputState(whoInput, isWhoInputValid, input, chipGroup)
            2 -> updateInputState(nameInput, isNameInputValid, input, chipGroup)
            3 -> updateInputState(whereInput, isWhereInputValid, input, chipGroup)
            4 -> updateInputState(genreInput, isGenreInputValid, input, chipGroup)
        }
    }

    // input 업데이트, 유효성 검사
    private fun updateInputState(
        inputLiveData: MutableLiveData<String>,
        validLiveData: MutableLiveData<Boolean>,
        input : String,
        chipGroup: ChipGroup
    ) {
        inputLiveData.value = input
        validLiveData.value = isValidInput(input, chipGroup)
    }

    // 입력값 유효성
    private fun isValidInput(input: String, chipGroup: ChipGroup): Boolean {
        return !( isInvalidWord(input) || isDuplicateChipName(chipGroup, input))
    }

    // 칩 중복 검사
    private fun isDuplicateChipName(chipGroup: ChipGroup, input: String): Boolean {
        val plainInputText = removeEmoji(input)
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? CustomChipView
            val chipText = removeEmoji(chip?.getChip()?.text.toString())
            if (plainInputText.equals(chipText, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    // 이모지 제거
    private fun removeEmoji(chipName: String): String {
        return chipName.replace(Regex("[\\p{So}\\p{Cn}]"), "").trim()
    }

    // 모든 칩 그룹 선택 여부
    private fun updateAllGroupSelectedState() {
        isAllGroupSelected.value =
                    selectedChipGroups[0].value?.size in 1..2 &&
                    selectedChipGroups[1].value?.size in 1..2 &&
                    selectedChipGroups[2].value?.size in 1..2 &&
                    selectedChipGroups[3].value?.size == 1 // 장르는 최대 1개 선택 가능
    }

    // 올바른 단어인지. ex) 동ㅁ, 사ㄱ, ㅇㅇㅇ, ㄹㄷㅇㅁ
    private fun isInvalidWord(input: String): Boolean {
        val validKoreanWordRegex = Regex("^[가-힣]+$")
        return !validKoreanWordRegex.matches(input)
    }

    // chip의 선택/해제 처리
    fun setChipSelectedState(chipId : Int, chipGroupNumber:Int){
        // 현재 선택된 Chip 목록
        val selectedChips = selectedChipGroups[chipGroupNumber - 1].value?.toMutableSet() ?: mutableSetOf()

        // Genre는 최대 1개 선택
        if (chipGroupNumber==4) {
            selectedChips.clear()
            selectedChips.add(chipId)
        }else {
            if (selectedChips.contains(chipId)){
                selectedChips.remove(chipId)
            }else{
                if (selectedChips.size < 2) {
                    selectedChips.add(chipId)
                }else{
                    return
                }
            }
        }
        selectedChipGroups[chipGroupNumber - 1].value = selectedChips
    }


}