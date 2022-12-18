package aut.dipterv.word_gardner.widgets.hints

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetHintTextBinding
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType

class TextHint @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : HintWidget(context, attrs, defStyleRes, HintType.TEXT_HINT) {

    private val binding =
        WidgetHintTextBinding.inflate(LayoutInflater.from(context), this, true)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_show).apply {
            setTarget(binding.root)
        }
    override val hideAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.hint_hide).apply {
            setTarget(binding.root)
            this.doOnEnd {
                closed.value = hintType
            }
        }

    override fun close() {
        hideAnimation.start()
    }

    override fun loadRandomWord() {
        super.loadRandomWord()

        setWord(actualWordCard)
    }

    fun setWord(word: WordCardModel) {
        actualWordCard = word
        binding.textHintWord.text =
            (if (isNative) actualWordCard.nativeWord else actualWordCard.foreignWord)
        binding.executePendingBindings()
    }
}
