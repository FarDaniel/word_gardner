package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserStatisticsDto(
    @field:JsonProperty("test_identifier", required = true) var testIdentifier: Int,

    @field:JsonProperty("succeeded_time_average", required = true) var succeedTimeAverage: Float,

    @field:JsonProperty("vote_ratio", required = true) var voteRatio: Float,

    @field:JsonProperty("recall_score", required = true) var recallScore: Float
)
