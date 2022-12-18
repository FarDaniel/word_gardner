package aut.dipterv.word_gardner.ui.practice.screens.loaded_packs.word_card_recycler_view

import androidx.recyclerview.widget.RecyclerView
import aut.dipterv.word_gardner.databinding.ItemWordCardBinding

class WordCardViewHolder(private val binding: ItemWordCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(wordCardModel: WordCardModel) {
        binding.flipCardWidget.setupData(
            wordCardModel.foreignWord,
            wordCardModel.nativeWord,
            wordCardModel.backGround
        )
    }

}
