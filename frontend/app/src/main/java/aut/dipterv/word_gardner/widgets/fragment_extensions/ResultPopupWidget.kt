package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.databinding.WidgetResultPopupBinding

class ResultPopupWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private val binding =
        WidgetResultPopupBinding.inflate(LayoutInflater.from(context), this, true)

    val restartClicked = MutableLiveData<Unit>()
    val backClicked = MutableLiveData<Unit>()

    init {
        binding.widget = this
        binding.card.animate()
            .translationX(-1500f)
            .setDuration(0)
            .start()
    }

    fun restart() {
        restartClicked.value = Unit
        binding.blinder.animate()
            .alpha(0F)
            .withEndAction {
                binding.blinder.visibility = View.GONE
            }
            .setDuration(500)
            .start()
        binding.card.animate()
            .translationX(-1500f)
            .withEndAction { restartClicked.value = Unit }
            .setDuration(800)
            .start()
    }

    fun back() {
        backClicked.value = Unit
        binding.card.animate()
            .translationX(1500f)
            .withEndAction { restartClicked.value = Unit }
            .setDuration(800)
            .start()
    }

    fun popUp(text: String) {
        binding.gratulationsSentense.text = text
        binding.blinder.visibility = View.VISIBLE
        binding.blinder.animate()
            .alpha(1F)
            .setDuration(500)
            .start()
        binding.card.animate()
            .translationX(0F)
            .setInterpolator(OvershootInterpolator())
            .setDuration(500)
            .start()
    }

}
