package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param wordCardId The identifier
 * @param inPackId The containing Pack
 * @param nativeWord The word on the user's mother language
 * @param foreignWord The word on the language, the user want to learn
 * @param background The color of the card. The user can use it, to logically group the cards
 */
@Entity
class WordCard(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long? = null,

    @field:JsonProperty("pack_id", required = true) val pack_id: Long,

    @field:JsonProperty("native_word", required = true) val nativeWord: String,

    @field:JsonProperty("foreign_word", required = true) val foreignWord: String,

    @field:JsonProperty("background", required = true) val background: Background
) {
    enum class Background(val value: String) {
        gray("gray"),
        red("red"),
        orange("orange"),
        green("green"),
        purple("purple"),
        blue("blue"),
        yellow("yellow");

    }

}

