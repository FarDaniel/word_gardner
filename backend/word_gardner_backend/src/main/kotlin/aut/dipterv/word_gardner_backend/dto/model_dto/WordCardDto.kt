package aut.dipterv.word_gardner_backend.dto.model_dto

import com.fasterxml.jackson.annotation.JsonProperty
import aut.dipterv.word_gardner_backend.model.WordCard

class WordCardDto(
    @field:JsonProperty("id", required = true) val id: Long,

    @field:JsonProperty("pack_id", required = true) val pack_id: Long?,

    @field:JsonProperty("native_word", required = true) val nativeWord: String?,

    @field:JsonProperty("foreign_word", required = true) val foreignWord: String?,

    @field:JsonProperty("background", required = true) val background: WordCard.Background?
)