package aut.dipterv.word_gardner.network_data.models

import aut.dipterv.word_gardner.local_data.models.Statistics
import com.google.gson.annotations.SerializedName

data class WordStatistics(
    @SerializedName("statistics_id")
    val statisticsId: Long,
    @SerializedName("succeeded")
    val succeeded: Int,
    @SerializedName("succeeded_time_average")
    val succeededTimeAverage: Float,
    @SerializedName("faulted")
    val faulted: Int,
    @SerializedName("faulted_time_average")
    val faultedTimeAverage: Float,
    @SerializedName("foreign_word")
    val foreignWord: String
) {
    fun toLocal(): Statistics {
        return Statistics(
            testIdentifier = 1111,
            foreignWord = foreignWord,
            succeeded = succeeded,
            succeededTimeAverage = succeededTimeAverage,
            faulted = faulted,
            faultedTimeAverage = faultedTimeAverage
        )
    }
}
