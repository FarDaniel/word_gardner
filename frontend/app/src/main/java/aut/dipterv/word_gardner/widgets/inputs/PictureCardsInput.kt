package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputPictureCardBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption
import aut.dipterv.word_gardner.network_data.UnsplashRetrofitFactory
import aut.dipterv.word_gardner.network_data.interactors.UnsplashInteractor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget
import com.bumptech.glide.Glide

class PictureCardsInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.PICTURE_CARD_INPUT) {
    private val binding =
        WidgetInputPictureCardBinding.inflate(LayoutInflater.from(context), this, true)

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

    override fun close() {
        hideAnimation.start()
    }

    private val unsplashInteractor: UnsplashInteractor =
        UnsplashRetrofitFactory.getUnsplashInteractor(context)
    var invalidatedCnt = 0

    private val optionsDeck = ArrayList<WordCardModel>()

    private var firstCardModel: WordCardModel = WordCardModel("", "", ColorOption.CardColor.GRAY)
        set(value) {
            val pictureDisposable = unsplashInteractor.getPicture(value.foreignWord).subscribe({
                var image: String
                it?.let {
                    if (it.results.isNotEmpty()) {
                        image = it.results.first().urls.smallPictureUrl
                        Glide.with(context)
                            .load(image)
                            .into(binding.firstCardImage)
                    } else {
                        binding.firstCardWord.text = value.nativeWord
                    }
                }
            }, {
                binding.firstCardWord.text = value.nativeWord
            }
            )
            field = value
        }
    private var secondCardModel: WordCardModel = WordCardModel("", "", ColorOption.CardColor.GRAY)
        set(value) {
            val pictureDisposable = unsplashInteractor.getPicture(value.foreignWord).subscribe({
                var image: String
                it?.let {
                    if (it.results.isNotEmpty()) {
                        image = it.results.first().urls.smallPictureUrl
                        Glide.with(context)
                            .load(image)
                            .into(binding.secondCardImage)
                    } else {
                        binding.secondCardWord.text = value.nativeWord
                    }
                }
            }, {
                binding.secondCardWord.text = value.nativeWord
            }
            )
            field = value
        }
    private var thirdCardModel: WordCardModel = WordCardModel("", "", ColorOption.CardColor.GRAY)
        set(value) {
            val pictureDisposable = unsplashInteractor.getPicture(value.foreignWord).subscribe({
                var image: String
                it?.let {
                    if (it.results.isNotEmpty()) {
                        image = it.results.first().urls.smallPictureUrl
                        Glide.with(context)
                            .load(image)
                            .into(binding.thirdCardImage)
                    } else {
                        binding.thirdCardWord.text = value.nativeWord
                    }
                }
            }, {
                binding.thirdCardWord.text = value.nativeWord
            }
            )
            field = value
        }
    private var fourthCardModel: WordCardModel = WordCardModel("", "", ColorOption.CardColor.GRAY)
        set(value) {
            val pictureDisposable = unsplashInteractor.getPicture(value.foreignWord).subscribe({
                var image: String
                it?.let {
                    if (it.results.isNotEmpty()) {
                        image = it.results.first().urls.smallPictureUrl
                        Glide.with(context)
                            .load(image)
                            .into(binding.fourthCardImage)
                    } else {
                        binding.fourthCardWord.text = value.nativeWord
                    }
                }
            }, {
                binding.fourthCardWord.text = value.nativeWord
            }
            )
            field = value
        }

    init {
        binding.widget = this
    }

    fun firstClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(firstCardModel.foreignWord, false)) {
            hintWidget.guess(firstCardModel.foreignWord, isAnswer = true, isNative = false)
            loadCards()
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(firstCardModel.foreignWord, isAnswer = true, isNative = false)
                loadCards()
            } else {
                binding.cardsFirstCard.visibility = View.INVISIBLE
            }
        }
        if (invalidatedCnt >= 4) {
            invalidatedCnt = 0
            loadCards()
        }
    }

    fun secondClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(secondCardModel.foreignWord, false)) {
            hintWidget.guess(secondCardModel.foreignWord, isAnswer = true, isNative = false)
            loadCards()
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(secondCardModel.foreignWord, isAnswer = true, isNative = false)
                loadCards()
            } else {
                binding.cardsSecondCard.visibility = View.INVISIBLE
            }
        }
        if (invalidatedCnt >= 4) {
            invalidatedCnt = 0
            loadCards()
        }
    }

    fun thirdClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(thirdCardModel.foreignWord, false)) {
            hintWidget.guess(thirdCardModel.foreignWord, isAnswer = true, isNative = false)
            loadCards()
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(thirdCardModel.foreignWord, isAnswer = true, isNative = false)
                loadCards()
            } else {
                binding.cardsThirdCard.visibility = View.INVISIBLE
            }
        }
        if (invalidatedCnt >= 4) {
            invalidatedCnt = 0
            loadCards()
        }
    }

    fun fourthClicked() {
        invalidatedCnt++
        if (hintWidget.checkWord(fourthCardModel.foreignWord, false)) {
            hintWidget.guess(fourthCardModel.foreignWord, isAnswer = true, isNative = false)
            loadCards()
        } else {
            if (invalidatedCnt >= 2) {
                hintWidget.guess(fourthCardModel.foreignWord, isAnswer = true, isNative = false)
                loadCards()
            } else {
                binding.cardsFourthCard.visibility = View.INVISIBLE
            }
        }
        if (invalidatedCnt >= 4) {
            loadCards()
        }
    }

    private fun loadCards() {
        invalidatedCnt = 0
        binding.cardsFirstCard.visibility = View.VISIBLE
        binding.cardsSecondCard.visibility = View.VISIBLE
        binding.cardsThirdCard.visibility = View.VISIBLE
        binding.cardsFourthCard.visibility = View.VISIBLE

        val cardSet = hintWidget.getOptions(4)

        firstCardModel = cardSet[0]
        secondCardModel = cardSet[1]
        thirdCardModel = cardSet[2]
        fourthCardModel = cardSet[3]

        binding.executePendingBindings()
    }

    override fun setHintWidget(hintWidget: HintWidget) {
        super.setHintWidget(hintWidget)

        loadCards()
    }
}


