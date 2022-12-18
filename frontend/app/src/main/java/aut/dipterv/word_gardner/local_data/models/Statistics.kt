package aut.dipterv.word_gardner.local_data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Statistics(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val testIdentifier: Int,
    val foreignWord: String,
    val succeeded: Int,
    val succeededTimeAverage: Float,
    val faulted: Int,
    val faultedTimeAverage: Float
)
