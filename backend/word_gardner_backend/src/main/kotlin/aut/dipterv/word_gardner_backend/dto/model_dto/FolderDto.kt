package aut.dipterv.word_gardner_backend.dto.model_dto

import aut.dipterv.word_gardner_backend.model.Folder
import com.fasterxml.jackson.annotation.JsonProperty

class FolderDto(
    @field:JsonProperty("id", required = true) val id: Long,

    @field:JsonProperty("user_id", required = true) val user_id: Long?,

    @field:JsonProperty("up_votes", required = true) val upVotes: Int?,

    @field:JsonProperty("down_votes", required = true) val downVotes: Int?,

    @field:JsonProperty("category", required = true) val category: String?,

    @field:JsonProperty("folder_name", required = true) val folderName: String?,

    @field:JsonProperty("is_own", required = true) var isOwn: Boolean? = null,

    @field:JsonProperty("difficulty", required = true) val difficulty: Folder.Difficulty?
) {
    companion object {
        fun fromFolder(folder: Folder, isOwn: Boolean?): FolderDto {
            return FolderDto(
                id = folder.id?: -1,
                user_id = folder.user_id,
                upVotes = folder.upVotes,
                downVotes = folder.downVotes,
                category = folder.category,
                folderName = folder.folderName,
                isOwn = isOwn,
                difficulty = folder.difficulty
            )
        }
    }
}