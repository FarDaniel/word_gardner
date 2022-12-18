package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class StatisticsModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("succeeded")
    val succeeded: Int,
    @SerializedName("faulted")
    val faulted: Int,
    @SerializedName("recall_statistics")
    val recall_statistics: Long,
    @SerializedName("succeed_time_average")
    val succeed_time_average: Long,
    @SerializedName("test_identifier")
    val testIdentifier: Int
)
