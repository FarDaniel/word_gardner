package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param packId The identifier
 * @param userId The identifier for the user who created the pack
 * @param upVotes The number of users, who upvoted the pack
 * @param downVotes The number of users, who downvoted the pack
 * @param category The assosiation to group the pack, with other packs.
 * @param packName The assosiation to group the cards in the pack.
 * @param difficulty How hard the words in the pack are
 */
@Entity
class Pack(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long? = null,

    @field:JsonProperty("user_id", required = true) var user_id: Long,

    @field:JsonProperty("up_votes", required = true) var upVotes: Int,

    @field:JsonProperty("down_votes", required = true) var downVotes: Int,

    @field:JsonProperty("category", required = true) val category: String,

    @field:JsonProperty("pack_name", required = true) val packName: String,

    @field:JsonProperty("difficulty", required = true) val difficulty: Difficulty
) {

    /**
     * How hard the words in the pack are
     * Values: easy,medium,hard
     */
    enum class Difficulty(val value: String) {
        easy("easy"),
        medium("medium"),
        hard("hard");

    }

}

