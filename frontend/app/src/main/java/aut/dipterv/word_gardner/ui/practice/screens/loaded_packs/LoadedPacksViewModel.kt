package aut.dipterv.word_gardner.ui.practice.screens.loaded_packs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadedPacksViewModel @Inject constructor(private val dao: WordPackDao) : ViewModel() {
    val cardList = MutableLiveData<ArrayList<WordCardModel>>()
    val navigateToFallback = MutableLiveData<Unit>()

    fun getCardsFromDao(packIds: ArrayList<Long>) {
        viewModelScope.launch {
            try {
                val cards = ArrayList<WordCardModel>()
                packIds.forEach { id ->
                    val wordCards = dao.getWordCardsOfPack(id)
                    wordCards.first().wordCards.forEach {
                        cards.add(
                            WordCardModel(
                                foreignWord = it.foreignWord,
                                nativeWord = it.nativeWord,
                                backGround = it.backGround
                            )
                        )
                    }
                }
                cards.shuffle()
                cardList.value = cards
            } catch (e: Exception) {
                if (cardList.value == null ||
                    cardList.value?.isEmpty() == true
                ) {
                    navigateToFallback.value = Unit
                } else {
                    e.printStackTrace()
                }
            }
        }
    }

}