package aut.dipterv.word_gardner.local_data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty

@Entity
data class Pack(
    @PrimaryKey(autoGenerate = true)
    val packId: Long = 0,
    val onlineId: Long? = null,
    val category: String,
    val packName: String,
    val difficulty: Difficulty
)