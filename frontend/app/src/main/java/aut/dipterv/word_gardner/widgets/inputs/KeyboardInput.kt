package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputKeyboardBinding
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget
import com.google.android.material.textfield.TextInputEditText

class KeyboardInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : InputWidget(context, attrs, defStyleRes, InputType.KEYBOARD_INPUT) {
    private val binding =
        WidgetInputKeyboardBinding.inflate(LayoutInflater.from(context), this, true)

    override val showAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.input_show).apply {
            setTarget(binding.root)
            this.doOnEnd {
                editText.requestFocus()
                inputMethodManager.showSoftInput(editText, 0)
            }
        }

    override val hideAnimation: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.input_hide).apply {
            setTarget(binding.root)
            this.doOnStart {
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            }
            this.doOnEnd {
                closed.value = inputType
            }
        }

    init {
        binding.widget = this
    }

    override fun reset() {
        super.reset()
        binding.editText.setText("")
    }

    override fun close() {
        hideAnimation.start()
    }

    override fun setHintWidget(hintWidget: HintWidget) {
        super.setHintWidget(hintWidget)

        binding.language.text = if (isNative) "Magyar" else "Angol"
    }

    private val editText: TextInputEditText = binding.editText
    private val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun doneWriting() {
        hintWidget.guess(binding.editText.text.toString(), true, isNative)
    }

}
