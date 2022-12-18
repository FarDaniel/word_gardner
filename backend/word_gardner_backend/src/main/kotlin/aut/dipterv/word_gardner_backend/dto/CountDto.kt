package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CountDto (
    @field:JsonProperty("count", required = true) val count: Int
        )