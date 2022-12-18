package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.Statistics
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StatisticsRepository : CrudRepository<Statistics, Long>{

    @Query("SELECT * FROM statistics WHERE user_id = ?1 AND  test_identifier = ?2 ", nativeQuery = true)
    fun getStatisticks(userId: Long, testIdentifier: Int): List<Statistics>

    @Query("SELECT * FROM statistics WHERE user_id = ?1 ", nativeQuery = true)
    fun getStatisticks(userId: Long): List<Statistics>
}
