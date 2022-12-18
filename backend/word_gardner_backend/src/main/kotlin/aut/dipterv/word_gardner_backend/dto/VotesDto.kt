package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VotesDto(
    @field:JsonProperty("up_votes", required = true) val upVotes: Int,

    @field:JsonProperty("down_votes", required = true) val downVotes: Int
)