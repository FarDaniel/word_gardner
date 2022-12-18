package aut.dipterv.word_gardner.widgets.hints

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetHintSoundBinding
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import java.util.*

class SoundHint @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : HintWidget(context, attrs, defStyleRes, HintType.SOUND_HINT),
    TextToSpeech.OnInitListener {
    private val binding =
        WidgetHintSoundBinding.inflate(LayoutInflater.from(context), this, true)

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

    private val reader = TextToSpeech(context, this)
    var isReaderReady = true

    init {
        binding.widget = this
    }

    override fun close() {
        hideAnimation.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = reader.setLanguage(Locale.UK)
            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                isReaderReady = false
            }
        } else {
            isReaderReady = false
        }
    }

    fun speak() {
        if (isReaderReady) {
            reader.speak(actualWordCard.foreignWord, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun loadRandomWord() {
        super.loadRandomWord()

        if (isReaderReady) {
            reader.speak(actualWordCard.foreignWord, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            binding.textHintWord.text = actualWordCard.foreignWord
            binding.executePendingBindings()
        }
    }

}