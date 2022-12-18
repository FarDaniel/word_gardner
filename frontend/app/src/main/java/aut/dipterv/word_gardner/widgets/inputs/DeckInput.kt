package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputDeckBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget

class DeckInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.DECK_INPUT) {

    companion object {
        private const val MAX_CARD = 8
    }

    val options = ArrayList<WordCardModel>()

    private val binding =
        WidgetInputDeckBinding.inflate(LayoutInflater.from(context), this, true)

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

    override fun setHintWidget(hintWidget: HintWidget) {
        super.setHintWidget(hintWidget)

        options.clear()
        options.addAll(hintWidget.getOptions(MAX_CARD))

        cardStartPlace = (binding.activeCard.layoutParams as RelativeLayout.LayoutParams).leftMargin
        activeCardModel = options.removeAt(0)
        secondCardModel = options.removeAt(0)

        binding.executePendingBindings()
    }

    init {
        makeListeners()
    }

    private var deltaVerticalPosition: Int = 0
    private var cardStartPlace: Int = 0

    private var activeCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.activeWord.text =
                (if (isNative) value.nativeWord else value.foreignWord)
            binding.activeColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }

    private var secondCardModel: WordCardModel = WordCardModel("", "", CardColor.GRAY)
        set(value) {
            binding.secondWord.text =
                (if (isNative) value.nativeWord else value.foreignWord)
            binding.secondColor.setBackgroundColor(value.backGround.colorCode)
            field = value
        }

    private fun loadCard() {
        activeCardModel = secondCardModel
        if (options.isNotEmpty()) {
            secondCardModel = options.removeAt(0)
        }

        binding.executePendingBindings()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun makeListeners() {
        binding.activeCard.setOnTouchListener { v, m ->
            val touchX = m.rawX.toInt()
            val params = (binding.activeCard.layoutParams as RelativeLayout.LayoutParams)

            when (m.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    deltaVerticalPosition = touchX - params.leftMargin
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.activeCard.animate()
                        .x(0F + touchX - deltaVerticalPosition)
                        .setDuration(0)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    if ((touchX - deltaVerticalPosition) > -cardStartPlace
                        && (touchX - deltaVerticalPosition) < (cardStartPlace * 4)
                    ) {
                        binding.activeCard.animate()
                            .x(0F + cardStartPlace)
                            .setDuration(250)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .start()
                    } else {
                        if ((touchX - deltaVerticalPosition) < -cardStartPlace) {

                            binding.activeCard.animate()
                                .x(0F - width)
                                .setDuration(150)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .withEndAction {
                                    Log.d("MAKING", "Aktív: " + activeCardModel)
                                    Log.d(
                                        "MAKING",
                                        "Hint: " + hintWidget.actualWordCard.foreignWord
                                    )
                                    hintWidget.guess(
                                        activeCardModel.foreignWord,
                                        isAnswer = false,
                                        isNative = false
                                    )
                                    loadCard()
                                    binding.activeCard.animate()
                                        .x(0F + cardStartPlace)
                                        .setDuration(0)
                                        .start()
                                }
                                .start()
                        } else {
                            if ((touchX - deltaVerticalPosition) > (cardStartPlace * 4)) {
                                binding.activeCard.animate()
                                    .x(0F + width)
                                    .setDuration(150)
                                    .setInterpolator(AccelerateDecelerateInterpolator())
                                    .withEndAction {
                                        hintWidget.guess(
                                            activeCardModel.foreignWord,
                                            isAnswer = true,
                                            isNative = false
                                        )
                                        loadCard()
                                        binding.activeCard.animate()
                                            .x(0F + cardStartPlace)
                                            .setDuration(0)
                                            .start()
                                    }
                                    .start()
                            }
                        }
                    }
                }
            }
            true
        }
    }
}
