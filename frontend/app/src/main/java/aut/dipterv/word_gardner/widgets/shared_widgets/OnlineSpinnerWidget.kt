package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.databinding.WidgetOnlineSpinnerBinding

class OnlineSpinnerWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private val binding =
        WidgetOnlineSpinnerBinding.inflate(LayoutInflater.from(context), this, true)
    var width: Float = 5000F
    var lastText = ""
    val reloadNeeded = MutableLiveData<Unit>()
    val navigateBack = MutableLiveData<Unit>()

    init {
        width = binding.root.width.toFloat()
        binding.widget = this
        binding.errorPopup.animate()
            .translationX(width + 1500F)
            .setDuration(0)
            .start()
    }

    fun disappear() {
        binding.root.visibility = View.GONE
    }

    fun reset() {
        binding.root.visibility = View.VISIBLE
    }

    fun popup(text: String) {
        binding.errorText.text = text
        lastText = text
        binding.errorPopup.animate()
            .translationX(-50F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(450).withEndAction {
                binding.errorPopup.animate()
                    .translationX(0F)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(150)
                    .start()
            }
            .start()
    }

    fun navigateBack() {
        navigateBack.value = Unit
    }

    fun reload() {
        reloadNeeded.value = Unit
    }
}