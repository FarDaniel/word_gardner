package aut.dipterv.word_gardner.ui.practice.screens.practice.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.analitycs.TestIdentifier
import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.LanguageLayout
import aut.dipterv.word_gardner.widgets.hints.*
import aut.dipterv.word_gardner.widgets.inputs.*
import aut.dipterv.word_gardner.widgets.inputs.letters_input.LettersInput
import kotlin.math.pow
import kotlin.random.Random

class InputToHintManager {
    val words = ArrayList<WordCardModel>()
    val hints = ArrayList<HintWidget>()
    val inputs = ArrayList<InputWidget>()

    var actualHint: HintWidget? = null
    var actualInput: InputWidget? = null
    var isMichrophoneNeeded = false

    var recommendations: HashMap<Int, Float>? = null

    fun makeHints(hintTypes: IntArray, context: Context): List<HintType> {
        hints.clear()
        val hintTypeList = ArrayList<Int>()
        if (hintTypes.isEmpty()) {
            HintType.values().forEach {
                hintTypeList.add(it.value)
            }
        } else {
            hintTypes.forEach {
                hintTypeList.add(it)
            }
        }
        val out = ArrayList<HintType>()
        hintTypeList.forEach {

            when (it) {
                HintType.HANGED_MAN_HINT.value -> {
                    hints.add(HangedManHint(context))
                    out.add(HintType.HANGED_MAN_HINT)
                }
                HintType.IMAGE_HINT.value -> {
                    hints.add(ImageHint(context))
                    out.add(HintType.IMAGE_HINT)
                }
                HintType.TEXT_HINT.value -> {
                    hints.add(TextHint(context))
                    out.add(HintType.TEXT_HINT)
                }
                HintType.SOUND_HINT.value -> {
                    hints.add(SoundHint(context))
                    out.add(HintType.SOUND_HINT)
                }
            }
        }
        if (words.size > 0) {
            hints.forEach {
                it.setCards(words)
            }
        }
        return out
    }

    fun makeInputs(inputTypes: IntArray, context: Context): List<InputType> {
        inputs.clear()
        val inputTypeList = ArrayList<Int>()
        if (inputTypes.isEmpty()) {
            InputType.values().forEach {
                inputTypeList.add(it.value)
            }
        } else {
            inputTypes.forEach {
                inputTypeList.add(it)
            }
        }
        val out = ArrayList<InputType>()
        inputTypeList.forEach {

            when (it) {
                InputType.CARDS_INPUT.value -> {
                    inputs.add(CardsInput(context))
                    out.add(InputType.CARDS_INPUT)
                }
                InputType.DECK_INPUT.value -> {
                    inputs.add(DeckInput(context))
                    out.add(InputType.DECK_INPUT)
                }
                InputType.KEYBOARD_INPUT.value -> {
                    inputs.add(KeyboardInput(context))
                    out.add(InputType.KEYBOARD_INPUT)
                }
                InputType.LETTERS_INPUT.value -> {
                    inputs.add(LettersInput(context))
                    out.add(InputType.LETTERS_INPUT)
                }
                InputType.SPEECH_INPUT.value -> {
                    inputs.add(SpeechInput(context))
                    out.add(InputType.SPEECH_INPUT)
                    isMichrophoneNeeded = true
                }
                InputType.PICTURE_CARD_INPUT.value -> {
                    inputs.add(PictureCardsInput(context))
                    out.add(InputType.PICTURE_CARD_INPUT)
                }
            }
        }
        return out
    }

    fun switchInput(): InputWidget? {
        inputs.shuffle()
        actualHint?.let {
            if (recommendations == null) {
                inputs.forEach { input ->
                    // Checking the language layout map of the hint
                    val languageLayout = it.hintType.languageLayout[input.inputType]
                    // If the hint and the input are compatible
                    if (languageLayout != null) {
                        actualInput = input
                        val pair = LanguageLayout.getLanguagePair(languageLayout)
                        setLanguage(pair)
                        loadWord()
                        actualInput?.setHintWidget(it)
                        actualInput?.showAnimation?.start()
                        return actualInput
                    }
                }
            } else {
                return recommendTo(it.hintType)
            }
        }
        return null
    }

    private fun setLanguage(pair: LanguagePair) {
        actualHint?.setLanguage(pair.isHintNative)
        actualInput?.setLanguage(pair.isInputNative)
    }

    fun closeHint() {
        actualHint?.close()
    }

    fun closeInput() {
        actualInput?.close()
    }

    private fun loadWord() {
        actualInput?.reset()
        actualHint?.reset()
        actualHint?.loadRandomWord()
    }

    fun switchHint(): HintWidget? {
        hints.shuffle()
        if (actualInput != null) {
            if (recommendations == null) {
                hints.forEach { hint ->
                    val languageLayout = hint.hintType.languageLayout[actualInput?.inputType
                        ?: InputType.CARDS_INPUT]
                    if (languageLayout != null) {
                        actualHint = hint
                        val pair = LanguageLayout.getLanguagePair(languageLayout)
                        setLanguage(pair)
                        loadWord()
                        actualInput?.setHintWidget(hint)
                        hint.showAnimation.start()
                        return hint
                    }
                }
            } else {
                return recommendTo(actualInput?.inputType)
            }
        } else {
            actualHint = hints.first()
            actualHint?.showAnimation?.start()
            return actualHint
        }
        return null
    }

    // Don't forget to call getInput and getHint after updated words
    fun setWords(words: List<WordCardModel>) {
        this.words.clear()
        this.words.addAll(words)
        this.hints.forEach {
            it.setCards(words)
        }
    }

    fun getTipLiveDatas(): List<MutableLiveData<TipModel>> {
        val outputs = ArrayList<MutableLiveData<TipModel>>()
        hints.forEach {
            outputs.add(it.tip)
        }
        return outputs
    }

    fun getCloseHintLiveDatas(): List<MutableLiveData<HintType>> {
        val outputs = ArrayList<MutableLiveData<HintType>>()
        hints.forEach {
            outputs.add(it.closed)
        }
        return outputs
    }

    fun getCloseInputLiveDatas(): List<MutableLiveData<InputType>> {
        val outputs = ArrayList<MutableLiveData<InputType>>()
        inputs.forEach {
            outputs.add(it.closed)
        }
        return outputs
    }

    fun getTestIdentifier(): TestIdentifier {
        return TestIdentifier(
            actualHint?.hintType,
            actualInput?.inputType,
            actualHint?.isNative ?: false,
            actualInput?.isNative ?: false
        )
    }

    fun recommendTo(hint: HintType?): InputWidget? {
        var sum = 0f
        val recommendationsArray = HashMap<Float, Int>()
        recommendations?.forEach {
            if (getDigit(4, it.key) == hint?.value) {
                recommendationsArray[sum] = it.key
                sum += it.value
            }
        }
        val random = Random.nextFloat() * sum
        var previous = 0
        var previousKey = 0f
        recommendationsArray.forEach {
            if (it.key < random && previousKey < it.key) {
                previous = it.value
                previousKey = it.key
            }
        }
        val pair = LanguagePair(
            getDigit(3, previous) == 1,
            getDigit(1, previous) == 1
        )
        val inputType = TestIdentifier.fromInt(previous).input
        actualInput = inputs.first()
        inputs.forEach {
            if (it.inputType == inputType) {
                actualInput = it
                return@forEach
            }
        }
        setLanguage(pair)
        loadWord()
        actualHint?.let {
            actualInput?.setHintWidget(it)
        }
        actualInput?.showAnimation?.start()
        return actualInput
    }

    fun recommendTo(input: InputType?): HintWidget {
        var sum = 0f
        val recommendationsArray = HashMap<Float, Int>()
        recommendations?.forEach {
            if (getDigit(2, it.key) == input?.value) {
                recommendationsArray[sum] = it.key
                sum += it.value
            }
        }
        val random = Random.nextFloat() * sum
        var previous = 0
        var previousKey = 0f
        recommendationsArray.forEach {
            if (it.key < random && previousKey < it.key) {
                previous = it.value
                previousKey = it.key
            }
        }
        val pair = LanguagePair(
            getDigit(3, previous) == 1,
            getDigit(1, previous) == 1
        )

        val hintType = TestIdentifier.fromInt(previous).hint
        hints.forEach {
            if (it.hintType == hintType) {
                actualHint = it
                return@forEach
            }
        }
        val hint = actualHint ?: hints.first()
        setLanguage(pair)
        loadWord()
        actualInput?.setHintWidget(hint)
        hint.showAnimation.start()
        return hint

    }

    fun setupRecommendations(recommendations: HashMap<Int, Float>?) {
        this.recommendations = recommendations
    }

    private fun getDigit(index: Int, number: Int): Int {
        val div = 10.0.pow(index).toInt()
        return (number / div) % 10
    }

}
