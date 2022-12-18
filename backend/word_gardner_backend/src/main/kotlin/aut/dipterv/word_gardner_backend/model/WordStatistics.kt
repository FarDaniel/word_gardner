package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class WordStatistics(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @field:JsonProperty("id", required = true) val id: Long,

        @field:JsonProperty("statistics_id", required = true) val statisticsId: Long,

        @field:JsonProperty("succeeded", required = true) val succeeded: Int,

        @field:JsonProperty("succeeded_time_average", required = true) val succeedTimeAverage: Float,

        @field:JsonProperty("faulted", required = true) val faulted: Int,

        @field:JsonProperty("faulted_time_average", required = true) val faultedTimeAverage: Float,

        @field:JsonProperty("foreign_word", required = true) val foreignWord: String
)
