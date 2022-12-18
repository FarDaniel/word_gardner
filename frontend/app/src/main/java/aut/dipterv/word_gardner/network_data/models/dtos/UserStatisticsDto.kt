package aut.dipterv.word_gardner.network_data.models.dtos

import com.google.gson.annotations.SerializedName

data class UserStatisticsDto(
    @SerializedName("test_identifier")
    val testIdentifier: Int,

    @SerializedName("succeeded_time_average")
    val succeedTimeAverage: Float,

    @SerializedName("vote_ratio")
    val voteRatio: Float,

    @SerializedName("recall_score")
    val recallScore: Float
)
