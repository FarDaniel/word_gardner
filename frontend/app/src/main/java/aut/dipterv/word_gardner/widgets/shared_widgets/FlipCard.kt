package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import aut.dipterv.word_gardner.databinding.WidgetFlipCardBinding
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption

class FlipCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    private val binding =
        WidgetFlipCardBinding.inflate(LayoutInflater.from(context), this, true)

    var isFirstSideActive = true
    private var firstSide: String = ""
    private var secondSide: String = ""
    var backgroundColor = ColorOption.CardColor.GRAY

    companion object {
        private const val ANIMATION_DURATION = 300L
    }

    fun setupData(firstWord: String, secondWord: String, backgroundColor: ColorOption.CardColor) {
        this.firstSide = firstWord
        this.secondSide = secondWord
        binding.backgroundColor.setCardBackgroundColor(backgroundColor.colorCode)
        binding.activeWord.text = firstWord
        binding.executePendingBindings()
    }

    init {
        binding.widget = this
    }

    fun flip() {
        isFirstSideActive = !isFirstSideActive
        binding.root.animate()
            .scaleX(0f)
            .setDuration(150)
            .withEndAction {
                binding.activeWord.text = if (isFirstSideActive) {
                    firstSide
                } else {
                    secondSide
                }
                binding.root.animate()
                    .scaleX(1f)
                    .setDuration(150)
                    .start()
            }.start()
    }

}
