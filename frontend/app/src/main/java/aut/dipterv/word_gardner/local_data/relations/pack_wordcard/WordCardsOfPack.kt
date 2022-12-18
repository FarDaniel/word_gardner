package aut.dipterv.word_gardner.local_data.relations.pack_wordcard

import androidx.room.Embedded
import androidx.room.Relation
import aut.dipterv.word_gardner.local_data.models.Pack
import aut.dipterv.word_gardner.local_data.models.WordCard

data class WordCardsOfPack(
    @Embedded val pack: Pack,
    @Relation(
        parentColumn = "packId",
        entityColumn = "inPackId"
    )
    val wordCards: List<WordCard>
)