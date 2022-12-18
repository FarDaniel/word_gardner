package aut.dipterv.word_gardner_backend.dto.model_dto

import aut.dipterv.word_gardner_backend.model.Pack
import com.fasterxml.jackson.annotation.JsonProperty

class PackDto(
    @field:JsonProperty("id", required = true) val id: Long,

    @field:JsonProperty("user_id", required = true) val user_id: Long?,

    @field:JsonProperty("up_votes", required = true) val upVotes: Int?,

    @field:JsonProperty("down_votes", required = true) val downVotes: Int?,

    @field:JsonProperty("category", required = true) val category: String?,

    @field:JsonProperty("pack_name", required = true) val packName: String?,

    @field:JsonProperty("is_own", required = true) var isOwn: Boolean? = null,

    @field:JsonProperty("difficulty", required = true) val difficulty: Pack.Difficulty?
) {
    companion object {
        fun fromPack(pack: Pack, isOwn: Boolean?): PackDto {
            return PackDto(
                id = pack.id ?: -1,
                user_id = pack.user_id,
                upVotes = pack.upVotes,
                downVotes = pack.downVotes,
                category = pack.category,
                packName = pack.packName,
                isOwn = isOwn,
                difficulty = pack.difficulty
            )
        }
    }
}