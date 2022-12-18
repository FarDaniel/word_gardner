package aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view

import android.view.View
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor

class NewWordModel(
    var nativeWord: String,
    var foreignWord: String,
    var backGround: CardColor,
    var editMenuVisible: Int = View.GONE
)
