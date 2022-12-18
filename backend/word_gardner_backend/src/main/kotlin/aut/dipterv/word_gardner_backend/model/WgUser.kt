package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param userId The identifier
 * @param score The points collected by this user
 * @param upVotes The number of upvotes on this user's Packs
 * @param downVotes The number of downvote on this user's Packs
 * @param name The name of the user
 * @param picture The profile picture of the user
 */
@Entity
@Table(name = "wg_user")
class WgUser(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long? = null,

    @field:JsonProperty("up_votes", required = true) var upVotes: Int,

    @field:JsonProperty("down_votes", required = true) var downVotes: Int,

    @field:JsonProperty("name", required = true) val name: String,

    @field:JsonProperty("password", required = true) val password: String,

    @field:JsonProperty("picture", required = false) val picture: String? = null
)
