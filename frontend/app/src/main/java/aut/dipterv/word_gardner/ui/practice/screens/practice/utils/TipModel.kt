package aut.dipterv.word_gardner.ui.practice.screens.practice.utils

import aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view.WordCardModel

data class TipModel(
    val word: WordCardModel,
    val isCorrect: Boolean
)
