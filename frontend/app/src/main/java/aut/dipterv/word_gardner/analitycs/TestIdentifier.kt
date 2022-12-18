package aut.dipterv.word_gardner.analitycs

import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType

data class TestIdentifier(
    val hint: HintType?,
    val input: InputType?,
    val isHintNative: Boolean,
    val isInputNative: Boolean,
) {

    companion object {
        fun fromInt(int: Int): TestIdentifier {
            val hint = when (int / 1000) {
                HintType.TEXT_HINT.value -> {
                    HintType.TEXT_HINT
                }
                HintType.HANGED_MAN_HINT.value -> {
                    HintType.HANGED_MAN_HINT
                }
                HintType.IMAGE_HINT.value -> {
                    HintType.IMAGE_HINT
                }
                HintType.SOUND_HINT.value -> {
                    HintType.TEXT_HINT
                }
                else -> {
                    HintType.TEXT_HINT
                }
            }
            val input = when ((int / 10) % 10) {
                InputType.DECK_INPUT.value -> {
                    InputType.DECK_INPUT
                }
                InputType.CARDS_INPUT.value -> {
                    InputType.CARDS_INPUT
                }
                InputType.LETTERS_INPUT.value -> {
                    InputType.LETTERS_INPUT
                }
                InputType.KEYBOARD_INPUT.value -> {
                    InputType.KEYBOARD_INPUT
                }
                InputType.SPEECH_INPUT.value -> {
                    InputType.SPEECH_INPUT
                }
                InputType.PICTURE_CARD_INPUT.value -> {
                    InputType.PICTURE_CARD_INPUT
                }
                else -> {
                    InputType.CARDS_INPUT
                }

            }

            val isHintNative = ((int / 100) % 10) == 1
            val isInputNative = (int % 10) == 1

            return TestIdentifier(hint, input, isHintNative, isInputNative)
        }

        fun getIdentifierInts(hint: HintType, input: InputType): List<Int> {
            val outList = ArrayList<Int>()
            hint.languageLayout[input]?.allPossibleLayout?.forEach {
                outList.add(TestIdentifier(hint, input, it.isHintNative, it.isInputNative).toInt())
            }
            return outList
        }

    }

    fun toInt(): Int {
        return if (hint != null && input != null)
            hint.value * 1000 + (if (isHintNative) 1 else 0) * 100 +
                    input.value * 10 + (if (isInputNative) 1 else 0)
        else
            -1
    }
}
