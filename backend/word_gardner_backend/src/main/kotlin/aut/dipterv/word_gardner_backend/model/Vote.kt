package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "vote")
class Vote(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long,

    @field:JsonProperty("from_user", required = true) var fromUser: Long,

    @field:JsonProperty("to_user", required = true) var toUser: Long,

    @field:JsonProperty("is_upvote", required = true) var isUpvote: Boolean,

    @field:JsonProperty("on_folder", required = false) var onFolder: Long?,

    @field:JsonProperty("on_pack", required = false) var onPack: Long?
)
