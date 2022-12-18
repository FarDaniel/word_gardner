package aut.dipterv.word_gardner.network_data.models

import aut.dipterv.word_gardner.local_data.dataenums.Difficulty
import aut.dipterv.word_gardner.local_data.models.Folder
import com.google.gson.annotations.SerializedName

data class FolderModel(
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
    @SerializedName("folder_name")
    val folderName: String,
    @SerializedName("difficulty")
    val difficulty: String
) : BackendModel() {

    companion object {
        fun fromLocal(folder: Folder): FolderModel {
            return FolderModel(
                id = -1,
                userId = -1,
                upVotes = 0,
                downVotes = 0,
                category = folder.category,
                folderName = folder.folderName,
                difficulty = when (folder.difficulty) {
                    Difficulty.EASY -> "easy"
                    Difficulty.MEDIUM -> "medium"
                    Difficulty.HARD -> "hard"
                    else -> {
                        "easy "
                    }
                }
            )
        }
    }

    fun toLocal(): Folder {
        return Folder(
            onlineId = id,
            category = category,
            folderName = folderName,
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
