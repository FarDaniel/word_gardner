package aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums

import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.LanguagePair
import kotlin.random.Random

object LanguageLayout {
    // Native to native cases don't make sense in language learning.
    enum class Layout(val allPossibleLayout: List<LanguagePair>) {
        NATIVE_TO_FOREIGN(listOf(LanguagePair(true, false))),
        FOREIGN_TO_NATIVE(listOf(LanguagePair(false, true))),
        FOREIGN_TO_FOREIGN(listOf(LanguagePair(false, false))),
        FOREIGN_TO_BOTH(
            listOf(
                LanguagePair(false, true),
                LanguagePair(false, false)
            )
        ),
        BOTH_TO_FOREIGN(
            listOf(
                LanguagePair(true, false),
                LanguagePair(false, false)
            )
        ),
        BOTH_TO_BOTH(
            listOf(
                LanguagePair(false, false),
                LanguagePair(false, true),
                LanguagePair(true, false)
            )
        ),
        BOTH_TO_OTHER(
            listOf(
                LanguagePair(false, true),
                LanguagePair(true, false)
            )
        )
    }

    fun getLanguagePair(languageLayout: Layout): LanguagePair {
        return when (languageLayout) {
            Layout.NATIVE_TO_FOREIGN -> {
                LanguagePair(isHintNative = true, isInputNative = false)
            }
            Layout.FOREIGN_TO_NATIVE -> {
                LanguagePair(isHintNative = false, isInputNative = true)
            }
            Layout.FOREIGN_TO_FOREIGN -> {
                LanguagePair(isHintNative = false, isInputNative = false)
            }
            Layout.FOREIGN_TO_BOTH -> {
                LanguagePair(false, Random.nextBoolean())
            }
            Layout.BOTH_TO_FOREIGN -> {
                LanguagePair(Random.nextBoolean(), false)
            }
            Layout.BOTH_TO_BOTH -> {
                val isHintNative = Random.nextBoolean()
                val isInputNative = Random.nextBoolean()

                // 75% for two different language
                if (isHintNative && isInputNative) {
                    if (Random.nextBoolean()) {
                        LanguagePair(isHintNative = true, isInputNative = false)
                    } else {
                        LanguagePair(isHintNative = false, isInputNative = true)
                    }
                } else {
                    LanguagePair(isHintNative, isInputNative)
                }
            }
            Layout.BOTH_TO_OTHER -> {
                if (Random.nextBoolean()) {
                    LanguagePair(isHintNative = true, isInputNative = false)
                } else {
                    LanguagePair(isHintNative = false, isInputNative = true)
                }
            }
        }
    }
}
