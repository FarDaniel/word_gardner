package aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums

enum class HintType(val value: Int, val languageLayout: HashMap<InputType, LanguageLayout.Layout>) {
    HANGED_MAN_HINT(
        0, hashMapOf(
            InputType.KEYBOARD_INPUT to LanguageLayout.Layout.BOTH_TO_BOTH,
            InputType.CARDS_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.LETTERS_INPUT to LanguageLayout.Layout.BOTH_TO_BOTH,
            InputType.DECK_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.SPEECH_INPUT to LanguageLayout.Layout.BOTH_TO_BOTH,
            InputType.PICTURE_CARD_INPUT to LanguageLayout.Layout.BOTH_TO_FOREIGN
        )
    ),
    IMAGE_HINT(
        1, hashMapOf(
            InputType.KEYBOARD_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN,
            InputType.CARDS_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN,
            InputType.LETTERS_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN,
            InputType.SPEECH_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN,
            InputType.DECK_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN
        )
    ),
    SOUND_HINT(
        2, hashMapOf(
            InputType.KEYBOARD_INPUT to LanguageLayout.Layout.FOREIGN_TO_BOTH,
            InputType.CARDS_INPUT to LanguageLayout.Layout.FOREIGN_TO_BOTH,
            InputType.LETTERS_INPUT to LanguageLayout.Layout.FOREIGN_TO_BOTH,
            InputType.DECK_INPUT to LanguageLayout.Layout.FOREIGN_TO_BOTH,
            InputType.SPEECH_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN,
            InputType.PICTURE_CARD_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN
        )
    ),
    TEXT_HINT(
        3, hashMapOf(
            InputType.KEYBOARD_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.CARDS_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.LETTERS_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.DECK_INPUT to LanguageLayout.Layout.BOTH_TO_OTHER,
            InputType.SPEECH_INPUT to LanguageLayout.Layout.BOTH_TO_BOTH,
            InputType.PICTURE_CARD_INPUT to LanguageLayout.Layout.FOREIGN_TO_FOREIGN
        )
    )
}
