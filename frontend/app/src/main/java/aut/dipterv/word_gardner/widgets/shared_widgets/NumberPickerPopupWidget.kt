package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.databinding.WidgetNumberPickerPopupBinding

class NumberPickerPopupWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private val binding =
        WidgetNumberPickerPopupBinding.inflate(LayoutInflater.from(context), this, true)
    val chosenNumber = MutableLiveData(1)

    init {
        binding.widget = this
        binding.numberPicker.minValue = 1
        binding.root.animate()
            .translationX(-1500f)
            .setDuration(0)
            .start()
    }

    fun popup(actualNumber: Int) {
        binding.numberPicker.value = actualNumber
        binding.root.animate()
            .translationX(0F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .start()
    }

    fun disappear() {
        binding.root.animate()
            .translationX(-1500F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(500)
            .start()
    }

    fun accept() {
        chosenNumber.value = binding.numberPicker.value
        disappear()
    }
}
