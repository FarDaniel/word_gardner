package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

class LoginDto(
    @field:JsonProperty("name", required = true) val name: String,

    @field:JsonProperty("password", required = true) val password: String,
)
