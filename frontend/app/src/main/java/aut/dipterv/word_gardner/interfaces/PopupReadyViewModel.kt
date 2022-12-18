package aut.dipterv.word_gardner.interfaces

import androidx.lifecycle.MutableLiveData

interface PopupReadyViewModel {
    var questionText: String
    var isPopupNeeded: MutableLiveData<Boolean>

    fun accepted()
    fun declined() {
        isPopupNeeded.value = false
    }
}