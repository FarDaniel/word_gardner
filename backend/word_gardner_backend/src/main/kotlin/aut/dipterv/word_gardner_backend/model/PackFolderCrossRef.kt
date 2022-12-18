package aut.dipterv.word_gardner_backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 *
 * @param id The identifier
 * @param packId The identifier for the pack
 * @param folderId folder
 */
@Entity
class PackFolderCrossRef(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:JsonProperty("id", required = true) var id: Long? = null,

    @field:JsonProperty("pack_id", required = true) val pack_id: Long,

    @field:JsonProperty("folder_id", required = true) val folder_id: Long
) {

}

