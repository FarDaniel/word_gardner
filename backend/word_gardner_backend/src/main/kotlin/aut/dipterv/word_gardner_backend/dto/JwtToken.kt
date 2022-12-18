package aut.dipterv.word_gardner_backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtToken(
    @field:JsonProperty("token") val token: String? = null,
    @field:JsonProperty("username") val username: String? = null,
    @field:JsonProperty("type") val type: String = "Bearer"
)
