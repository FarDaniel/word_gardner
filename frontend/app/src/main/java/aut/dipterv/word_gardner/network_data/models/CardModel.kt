package aut.dipterv.word_gardner.network_data.models

import aut.dipterv.word_gardner.local_data.dataenums.ColorOption
import aut.dipterv.word_gardner.local_data.models.WordCard
import com.google.gson.annotations.SerializedName

data class CardModel(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("pack_id")
    val inPackId: Long,
    @SerializedName("native_word")
    val nativeWord: String,
    @SerializedName("foreign_word")
    val foreignWord: String,
    @SerializedName("background")
    val backGround: String
) : BackendModel() {

    companion object {
        fun fromLocal(card: WordCard, onlinePackId: Long): CardModel {
            return CardModel(
                id = card.onlineId ?: -1,
                inPackId = onlinePackId,
                nativeWord = card.nativeWord,
                foreignWord = card.foreignWord,
                backGround = card.backGround.toString().lowercase()
            )
        }
    }

    fun toLocal(): WordCard {
        return WordCard(
            onlineId = id,
            inPackId = 0,
            nativeWord = nativeWord,
            foreignWord = foreignWord,
            backGround = ColorOption.getByName(backGround)
        )
    }
}
