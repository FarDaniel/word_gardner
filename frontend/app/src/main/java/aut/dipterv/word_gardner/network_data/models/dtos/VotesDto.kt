package aut.dipterv.word_gardner.network_data.models.dtos

import com.google.gson.annotations.SerializedName

data class VotesDto(
    @SerializedName("up_votes")
    val upVotes: Int,
    @SerializedName("down_votes")
    val downVotes: Int,
)