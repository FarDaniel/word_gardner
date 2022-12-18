package aut.dipterv.word_gardner.widgets.inputs.letters_input

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.GridLayoutManager
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputLettersBinding
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget
import aut.dipterv.word_gardner.widgets.inputs.InputWidget
import aut.dipterv.word_gardner.widgets.inputs.letters_input.letters_recycler_view.LettersAdapter

class LettersInput(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.LETTERS_INPUT) {
    var wordPart = ""

    private val binding =
        WidgetInputLettersBinding.inflate(LayoutInflater.from(context), this, true)
    private val adapter = LettersAdapter(this)

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

    init {
        val layoutManager = GridLayoutManager(context, 4, GridLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun reset() {
        super.reset()

        wordPart = ""
        binding.solution.text = ""
        binding.executePendingBindings()
    }

    override fun close() {
        hideAnimation.start()
    }

    override fun setHintWidget(hintWidget: HintWidget) {
        super.setHintWidget(hintWidget)

        binding.language.text = if (isNative) "Magyar" else "Angol"
        loadLetters()
    }

    private fun loadLetters() {
        wordPart = ""
        binding.solution.text = ""
        var actualWord = if (isNative) {
            hintWidget.actualWordCard.nativeWord
        } else {
            hintWidget.actualWordCard.foreignWord
        }

        if (actualWord.length < 16) {
            while (actualWord.length < 16) {
                actualWord += ('a'..'z').random()
            }
        } else {
            for (i in 0..10) {
                actualWord += ('a'..'z').random()
            }
        }
        val letters = ArrayList<Char>()
        actualWord.forEach {
            letters.add(it)
        }
        letters.shuffle()
        adapter.setFromWord(letters)
        binding.recyclerView.adapter = adapter
        binding.executePendingBindings()
    }

    fun checkLetter(letter: Char) {
        var actualWord = ""
        actualWord = if (isNative) {
            hintWidget.actualWordCard.nativeWord
        } else {
            hintWidget.actualWordCard.foreignWord
        }

        if (actualWord[wordPart.length] == letter) {
            wordPart += letter
            if (wordPart.length == actualWord.length) {
                hintWidget.guess(actualWord, true, isNative)
            }
        } else {
            hintWidget.guess(wordPart, true, isNative)
        }
        binding.solution.text = wordPart
        binding.executePendingBindings()
    }

}
