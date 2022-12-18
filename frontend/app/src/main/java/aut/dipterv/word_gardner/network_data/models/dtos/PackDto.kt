package aut.dipterv.word_gardner.network_data.models.dtos

import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Pack
import aut.dipterv.word_gardner.network_data.models.BackendModel
import aut.dipterv.word_gardner.network_data.models.PackModel
import com.google.gson.annotations.SerializedName

class PackDto(
    @SerializedName("id")
    override val id: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("up_votes")
    val upVotes: Int,
    @SerializedName("down_votes")
    val downVotes: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("pack_name")
    val packName: String,
    @SerializedName("is_own")
    val isOwn: Boolean,
    @SerializedName("difficulty")
    val difficulty: String
) : BackendModel() {

    companion object {
        fun fromLocal(pack: Pack): PackModel {
            return PackModel(
                id = pack.onlineId ?: -1,
                userId = -1,
                upVotes = 0,
                downVotes = 0,
                category = pack.category.lowercase(),
                packName = pack.packName.lowercase(),
                difficulty = pack.difficulty.toString().lowercase()
            )
        }
    }

    fun toLocal(): Pack {
        return Pack(
            onlineId = id,
            category = category,
            packName = packName,
            difficulty = when (difficulty) {
                "easy" -> Difficulty.EASY
                "medium" -> Difficulty.MEDIUM
                "hard" -> Difficulty.HARD
                else -> {
                    Difficulty.UNDEFINED
                }
            }
        )
    }
}