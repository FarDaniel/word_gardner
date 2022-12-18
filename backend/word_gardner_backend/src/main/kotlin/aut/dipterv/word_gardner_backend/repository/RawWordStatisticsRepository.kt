package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.RawWordStatistics
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RawWordStatisticsRepository : CrudRepository<RawWordStatistics, Long>{


    @Query(value = "SELECT * FROM raw_word_statistics LIMIT ?1 ", nativeQuery = true)
    fun getRawWordStatistics(limit: Int): List<RawWordStatistics>

    @Query(value = "SELECT * FROM raw_word_statistics WHERE statistics_id = ?1 ", nativeQuery = true)
    fun getRawWordStatistics(statisticsId: Long): List<RawWordStatistics>

}
