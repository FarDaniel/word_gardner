package aut.dipterv.word_gardner.local_data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import aut.dipterv.word_gardner.local_data.dataenums.Difficulty

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val folderId: Long = 0,
    val onlineId: Long? = null,
    val category: String,
    val folderName: String,
    val difficulty: Difficulty = Difficulty.UNDEFINED
)