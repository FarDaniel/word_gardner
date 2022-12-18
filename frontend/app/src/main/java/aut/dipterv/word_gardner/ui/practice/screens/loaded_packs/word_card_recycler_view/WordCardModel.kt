package aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view

import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor

data class WordCardModel(
    val foreignWord: String,
    val nativeWord: String,
    val backGround: CardColor
)