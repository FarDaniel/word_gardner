package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.WordCard
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WordCardRepository : CrudRepository<WordCard, Long> {

    @Query(value = "SELECT count(*) FROM word_card where pack_id = ?1 ", nativeQuery = true)
    fun getWordCardCnt(packId: Long): Int

    @Query(
        value = "SELECT * FROM word_card WHERE pack_id = ?1 LIMIT ?2 OFFSET ?3 ", nativeQuery = true
    )
    fun getCardsByPack(packId: Long, limit: Int, offset: Int): List<WordCard>

}
