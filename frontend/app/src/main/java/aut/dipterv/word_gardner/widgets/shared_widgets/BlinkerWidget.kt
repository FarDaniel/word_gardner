package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import aut.dipterv.word_gardner.databinding.WidgetBlinkerBinding

class BlinkerWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private val binding =
        WidgetBlinkerBinding.inflate(LayoutInflater.from(context), this, true)

    fun blinkRed() {
        binding.redImage.animate()
            .alpha(1f)
            .setDuration(100)
            .setInterpolator(FastOutSlowInInterpolator())
            .withEndAction {
                binding.redImage.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .setInterpolator(FastOutSlowInInterpolator()).start()
            }
            .start()
    }

    fun blinkGreen() {
        binding.greenImage.animate()
            .alpha(1f)
            .setDuration(100)
            .setInterpolator(FastOutSlowInInterpolator())
            .withEndAction {
                binding.greenImage.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .setInterpolator(FastOutSlowInInterpolator()).start()
            }
            .start()
    }

}
