package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param id The identifier
 * @param userId The statistics are about this user
 * @param succeeded The count of accurately guessed words
 * @param faulted The count of inaccurately guessed words
 * @param recallStatistics good answers divided by all weighted with time
 * @param succeedTimeAverage average ellapsed time between two succeeded guess
 * @param testIdentifier which hint was used
 */
@Entity
class Statistics(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) val id: Long,

    @field:JsonProperty("user_id", required = true) var userId: Long,

    @field:JsonProperty("test_identifier", required = true) val testIdentifier: Int
)

