package aut.dipterv.word_gardner.local_data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.ui.practice.screens.edit_pack.new_word_recycler_view.NewWordModel

@Entity
data class WordCard(
    @PrimaryKey(autoGenerate = true)
    val wordCardId: Long = 0,
    val onlineId: Long? = null,
    val inPackId: Long,
    val nativeWord: String,
    val foreignWord: String,
    val backGround: CardColor
) {
    companion object {

        fun fromNewWordModel(newWordModel: NewWordModel, packId: Long): WordCard {
            return WordCard(
                inPackId = packId,
                nativeWord = newWordModel.nativeWord,
                foreignWord = newWordModel.foreignWord,
                backGround = newWordModel.backGround
            )
        }
    }
}
