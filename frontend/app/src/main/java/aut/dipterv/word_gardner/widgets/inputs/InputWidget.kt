package aut.dipterv.word_gardner.widgets.inputs

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.widgets.hints.HintWidget

abstract class InputWidget(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    val inputType: InputType
) : ConstraintLayout(context, attrs, defStyleRes) {

    val closed = MutableLiveData<InputType>()
    internal lateinit var hintWidget: HintWidget
    internal abstract val showAnimation: Animator
    internal abstract val hideAnimation: Animator
    var isNative: Boolean = false

    fun setLanguage(isNative: Boolean) {
        this.isNative = isNative
    }

    open fun setHintWidget(hintWidget: HintWidget) {
        this.hintWidget = hintWidget
    }

    open fun reset() {
    }

    abstract fun close()
}