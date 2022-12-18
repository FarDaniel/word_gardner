package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputCardsBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget

class CardsInput(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.CARDS_INPUT) {

    private val binding =
        WidgetInputCardsBinding.inflate(LayoutInflater.from(context), this, true)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.input_show).apply {
            setTarget(binding.root)
        }
    override val hideAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.input_hide).apply {
            setTarget(binding.root)
            this.doOnEnd {
                closed.value = inputType
            }
        }

    var invalidatedCnt = 0

    private val optionsDeck = ArrayList<WordCardModel>()

    private var firstCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.firstCardWord.text = (if (isNative) value.nativeWord else value.foreignWord)
            binding.firstCardColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }
    private var secondCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.secondCardWord.text = (if (isNative) value.nativeWord else value.foreignWord)
            binding.secondCardColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }
    private var thirdCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.thirdCardWord.text = (if (isNative) value.nativeWord else value.foreignWord)
            binding.thirdCardColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }
    private var fourthCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.fourthCardWord.text = (if (isNative) value.nativeWord else value.foreignWord)
            binding.fourthCardColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }

    init {
        binding.widget = this
    }

    override fun reset() {
        super.reset()

        invalidatedCnt = 0
        optionsDeck.clear()
    }

    override fun close() {
        hideAnimation.start()
    }

    override fun setHintWidget(hintWidget: HintWidget) {
        super.setHintWidget(hintWidget)

        loadCards()
    }

    fun firstClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(firstCardModel.foreignWord, false)) {
            hintWidget.guess(firstCardModel.foreignWord, isAnswer = true, isNative = false)
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(firstCardModel.foreignWord, isAnswer = true, isNative = false)
            } else {
                binding.cardsFirstCard.visibility = View.INVISIBLE
            }
        }
    }

    fun secondClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(secondCardModel.foreignWord, false)) {
            hintWidget.guess(secondCardModel.foreignWord, isAnswer = true, isNative = false)
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(secondCardModel.foreignWord, isAnswer = true, isNative = false)
            } else {
                binding.cardsSecondCard.visibility = View.INVISIBLE
            }
        }
    }

    fun thirdClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(thirdCardModel.foreignWord, false)) {
            hintWidget.guess(thirdCardModel.foreignWord, isAnswer = true, isNative = false)
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(thirdCardModel.foreignWord, isAnswer = true, isNative = false)
            } else {
                binding.cardsThirdCard.visibility = View.INVISIBLE
            }
        }
    }

    fun fourthClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(fourthCardModel.foreignWord, false)) {
            hintWidget.guess(fourthCardModel.foreignWord, isAnswer = true, isNative = false)
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(fourthCardModel.foreignWord, isAnswer = true, isNative = false)
            } else {
                binding.cardsFourthCard.visibility = View.INVISIBLE
            }
        }
    }

    private fun loadCards() {
        invalidatedCnt = 0
        binding.cardsFirstCard.visibility = View.VISIBLE
        binding.cardsSecondCard.visibility = View.VISIBLE
        binding.cardsThirdCard.visibility = View.VISIBLE
        binding.cardsFourthCard.visibility = View.VISIBLE

        val cardSet = hintWidget.getOptions(4)
        Log.d("MAKING", cardSet.toString())
        Log.d("MAKING", hintWidget.actualWordCard.foreignWord)

        firstCardModel = cardSet[0]
        secondCardModel = cardSet[1]
        thirdCardModel = cardSet[2]
        fourthCardModel = cardSet[3]

        binding.executePendingBindings()
    }
}

