package aut.dipterv.word_gardner.widgets.hints

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.TipModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import kotlin.random.Random

abstract class HintWidget(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    val hintType: HintType
) : ConstraintLayout(context, attrs, defStyleRes) {
    val closed = MutableLiveData<HintType>()
    val tip = MutableLiveData<TipModel>()

    var actualWordCard = WordCardModel("", "", CardColor.GRAY)
    internal abstract val showAnimation: Animator
    internal abstract val hideAnimation: Animator
    var isNative: Boolean = false

    val words = ArrayList<WordCardModel>()

    fun setLanguage(isNative: Boolean) {
        this.isNative = isNative
    }

    fun setCards(wordCards: List<WordCardModel>) {
        words.addAll(wordCards)
    }

    open fun guess(word: String, isAnswer: Boolean, isNative: Boolean): Boolean {
        return if (isAnswer) {
            if (checkWord(word, isNative)) {
                tip.value = TipModel(actualWordCard, true)
                true
            } else {
                tip.value = TipModel(actualWordCard, false)
                false
            }
        } else {
            if (checkWord(word, isNative)) {
                tip.value = TipModel(actualWordCard, false)
                false
            } else {
                true
            }
        }
    }

    open fun checkWord(word: String, isNative: Boolean? = null): Boolean {
        return if (isNative == null) {
            word.lowercase() == actualWordCard.nativeWord.lowercase()
                    || word.lowercase() == actualWordCard.foreignWord.lowercase()
        } else {
            if (isNative) {
                word.lowercase() == actualWordCard.nativeWord.lowercase()
            } else {
                word.lowercase() == actualWordCard.foreignWord.lowercase()
            }
        }
    }

    open fun reset() {
    }

    open fun loadRandomWord() {
        if (words.size > 0) {
            actualWordCard = words[(0 until words.size).random()]
        } else {
            throw Exception("There are no words to learn!")
        }
    }

    open fun getOptions(size: Int): ArrayList<WordCardModel> {
        if (words.size > 0) {
            val optionsDeck = ArrayList<WordCardModel>()
            optionsDeck.add(actualWordCard)
            words.remove(actualWordCard)
            while (optionsDeck.size != size) {
                // So can't duplicate words
                if (words.isNotEmpty()) {
                    val random = Random.nextInt(words.size)
                    optionsDeck.add(words.removeAt(random))
                } else {
                    optionsDeck.add(optionsDeck[Random.nextInt(optionsDeck.size)])
                }
            }
            words.addAll(optionsDeck)
            optionsDeck.shuffle()
            return optionsDeck
        } else {
            throw Exception("There are no words to learn!")
        }
    }

    abstract fun close()
}
