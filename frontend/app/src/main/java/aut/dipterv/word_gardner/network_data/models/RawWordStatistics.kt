package aut.dipterv.word_gardner.network_data.models

import com.google.gson.annotations.SerializedName

data class RawWordStatistics(
    @SerializedName("id")
    val id: Long = -1,
    @SerializedName("statistics_id")
    val statisticsId: Long = -1,
    @SerializedName("test_identifier")
    val testIdentifier: Int,
    @SerializedName("is_succeeded")
    val succeeded: Boolean,
    @SerializedName("time")
    val time: Float,
    @SerializedName("foreign_word")
    val foreignWord: String
)