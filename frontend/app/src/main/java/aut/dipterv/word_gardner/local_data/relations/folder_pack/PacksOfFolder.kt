package aut.dipterv.word_gardner.local_data.relations.folder_pack

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import aut.dipterv.word_gardner.local_data.models.Folder
import aut.dipterv.word_gardner.local_data.models.Pack

data class PacksOfFolder(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "packId",
        associateBy = Junction(PackFolderCrossRef::class)
    )
    val packs: List<Pack>
)