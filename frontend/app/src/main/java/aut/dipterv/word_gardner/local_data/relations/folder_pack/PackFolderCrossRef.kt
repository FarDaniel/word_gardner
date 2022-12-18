package aut.dipterv.word_gardner.local_data.relations.folder_pack

import androidx.room.Entity

@Entity(primaryKeys = ["folderId", "packId"])
data class PackFolderCrossRef(
    val folderId: Long,
    val packId: Long
)