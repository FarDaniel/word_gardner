package aut.dipterv.word_gardner.widgets.fragment_extensions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.WidgetInputToHintPickerBinding

class InputToHintPickerWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    val binding = WidgetInputToHintPickerBinding.inflate(LayoutInflater.from(context), this, true)

    val hints = hashMapOf(0 to true, 1 to true, 2 to true, 3 to true)

    val inputs = hashMapOf(0 to true, 1 to true, 2 to true, 3 to true, 4 to true, 5 to true)

    init {
        binding.widget = this
    }

    fun switchHangedMan() {
        hints[0] = hints[0] != true
        if (hints[0] != false) {
            binding.hangedManImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.hangedManImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(true)
    }

    fun switchImage() {
        hints[1] = hints[1] != true
        if (hints[1] != false) {
            binding.imageImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.imageImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(true)
    }

    fun switchSound() {
        hints[2] = hints[2] != true
        if (hints[2] != false) {
            binding.soundImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.soundImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(true)
    }

    fun switchText() {
        hints[3] = hints[3] != true
        if (hints[3] != false) {
            binding.lettersImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.lettersImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(true)
    }

    fun switchCards() {

        inputs[0] = inputs[0] != true
        if (inputs[0] != false) {
            binding.cardsImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.cardsImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }

    fun switchDeck() {

        inputs[1] = inputs[1] != true
        if (inputs[1] != false) {
            binding.deckImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.deckImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }

    fun switchKeyboard() {

        inputs[2] = inputs[2] != true
        if (inputs[2] != false) {
            binding.keyboardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.keyboardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }

    fun switchLetters() {

        inputs[3] = inputs[3] != true
        if (inputs[3] != false) {
            binding.lettersInputImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.lettersInputImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }

    fun switchSpeech() {

        inputs[4] = inputs[4] != true
        if (inputs[4] != false) {
            binding.speechCardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.speechCardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }

    fun switchPictureCard() {

        inputs[5] = inputs[5] != true
        if (inputs[5] != false) {
            binding.pictureCardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.pictureCardImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.fifty_gray
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        checkIfEmpty(false)
    }


    private fun checkIfEmpty(isHint: Boolean) {
        var hasActive = false
        if (isHint) {
            hints.values.forEach {
                if (it) {
                    hasActive = true
                }
            }
            if (!hasActive) {
                switchHangedMan()
                switchImage()
                switchSound()
                switchText()
            }
        } else {
            inputs.values.forEach {
                if (it) {
                    hasActive = true
                }
            }
            if (!hasActive) {
                switchCards()
                switchDeck()
                switchKeyboard()
                switchLetters()
                switchSpeech()
                switchPictureCard()
            }
        }
    }

    fun getHintList(): List<Int> {
        val hintList = ArrayList<Int>()
        hints.forEach {
            if (it.value) {
                hintList.add(it.key)
            }
        }
        return hintList
    }

    fun getInputList(): List<Int> {
        val inputList = ArrayList<Int>()
        inputs.forEach {
            if (it.value) {
                inputList.add(it.key)
            }
        }
        return inputList
    }
}
