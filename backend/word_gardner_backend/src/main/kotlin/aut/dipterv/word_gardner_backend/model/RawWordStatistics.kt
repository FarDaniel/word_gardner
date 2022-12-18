package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RawWordStatistics(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) val id: Long,

    @field:JsonProperty("statistics_id", required = true) val statisticsId: Long,

    @field:JsonProperty("test_identifier", required = true) val testIdentifier: Int,

    @field:JsonProperty("is_succeeded", required = true) val succeeded: Boolean,

    @field:JsonProperty("time", required = true) val time: Float,

    @field:JsonProperty("foreign_word", required = true) val foreignWord: String
)