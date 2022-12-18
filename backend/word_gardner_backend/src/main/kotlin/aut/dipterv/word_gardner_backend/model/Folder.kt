package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param folderId The identifier
 * @param userId The identifier for the user who created the folder
 * @param upVotes The number of users, who upvoted the folder
 * @param downVotes The number of users, who downvoted the folder
 * @param category The assosiation to group the folder, with other folders.
 * @param folderName The assosiation to group the Packs in the folder.
 * @param difficulty How hard the words in the folder are
 */
@Entity
class Folder(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long? = null,

    @field:JsonProperty("user_id", required = true) var user_id: Long,

    @field:JsonProperty("up_votes", required = true) var upVotes: Int,

    @field:JsonProperty("down_votes", required = true) var downVotes: Int,

    @field:JsonProperty("category", required = true) val category: String,

    @field:JsonProperty("folder_name", required = true) val folderName: String,

    @field:JsonProperty("difficulty", required = true) val difficulty: Difficulty
) {

    enum class Difficulty(val value: String) {
        easy("easy"),
        medium("medium"),
        hard("hard");

    }

}

