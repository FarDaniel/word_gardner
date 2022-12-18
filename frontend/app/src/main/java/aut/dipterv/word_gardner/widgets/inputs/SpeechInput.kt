package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.core.animation.doOnEnd
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputSpeechBinding
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType


class SpeechInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.SPEECH_INPUT) {
    private val binding =
        WidgetInputSpeechBinding.inflate(LayoutInflater.from(context), this, true)
    private var fingersDown = 0
    private val speechRecogniser = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.input_show).apply {
            setTarget(binding.root)
            this.doOnEnd {
                binding.tipText.text = ""
            }
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

    override fun reset() {
        super.reset()
        fingersDown = 0
    }

    init {
        initListeners()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        binding.michrophoneButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (++fingersDown == 1) {
                        startListening()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (--fingersDown <= 0) {
                        fingersDown = 0
                        stopListening()
                    }
                }
            }
            true
        }
        speechRecogniser.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {
                binding.michrophoneButton.animate()
                    .translationY(0F)
                    .start()
            }

            override fun onError(p0: Int) {
                Log.e("MAKING", p0.toString())
                hintWidget.guess("", isAnswer = true, isNative = true)
            }

            override fun onResults(result: Bundle?) {
                val result = result?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.first().toString()
                if (hintWidget.checkWord(result)) {
                    hintWidget.guess(
                        hintWidget.actualWordCard.nativeWord,
                        isAnswer = true,
                        isNative = true
                    )
                } else {
                    hintWidget.guess(result, isAnswer = true, isNative = true)
                }
            }

            override fun onPartialResults(result: Bundle?) {
                hintWidget.guess("", isAnswer = true, isNative = true)
            }

            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
    }

    private fun stopListening() {
        speechRecogniser.stopListening()
        binding.michrophoneButton.animate()
            .translationY(0F)
            .start()
    }

    private fun startListening() {
        speechRecogniser.startListening(speechRecognizerIntent)
        binding.michrophoneButton.animate()
            .translationY(-binding.listeningText.height.toFloat())
            .start()
    }

}
