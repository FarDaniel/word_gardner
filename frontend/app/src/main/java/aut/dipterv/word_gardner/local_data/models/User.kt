package aut.dipterv.word_gardner.local_data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val onlineId: Long,
    val name: String,
    val password: String? = null,
    val picture: String? = null
)
