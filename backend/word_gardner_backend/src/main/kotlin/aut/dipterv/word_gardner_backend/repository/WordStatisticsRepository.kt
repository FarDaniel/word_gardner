package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.WordStatistics
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WordStatisticsRepository : CrudRepository<WordStatistics, Long> {

    @Query(value = "SELECT * FROM Word_Statistics WHERE statistics_id = ?1 AND foreign_word = ?2 ", nativeQuery = true)
    fun getWordStatistics(statisticsId: Long, foreignWord: String): List<WordStatistics>

    @Query(value = "SELECT * FROM Word_Statistics WHERE statistics_id = ?1 ", nativeQuery = true)
    fun getWordStatistics(statisticsId: Long): List<WordStatistics>

}
