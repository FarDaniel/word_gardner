package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.Folder
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository : CrudRepository<Folder, Long> {

    @Query(
        value = "Select folder.* from folder " +
                "   join wg_user wu on wu.id = folder.user_id " +
                "WHERE (folder_name like '%' || ?1 || '%') " +
                "    and (folder.category like '%' || ?2 || '%') " +
                "    and (nullif(?3, -1 ) is null or wu.id = ?3) " +
                "    and (?4 = 0 " +
                "   or (float8(folder.up_votes) / nullif(folder.up_votes + folder.down_votes, 0)) > ?4) " +
                "    and ((not (?5 or ?6 or ?7)) " +
                "   or (?5 and difficulty = 0) " +
                "   or (?6 and difficulty = 1) " +
                "   or (?7 and difficulty = 2)) " +
                "ORDER BY folder.up_votes / coalesce(nullif((folder.up_votes + folder.down_votes), 0), 1) desc, folder.up_votes desc, folder.id desc " +
                "LIMIT ?8 OFFSET ?9 ", nativeQuery = true
    )
    fun getFiltered(
        namePart: String,
        categoryPart: String,
        userId: Long,
        minimalUpvotePercentage: Float,
        easyIncluded: Boolean,
        mediumIncluded: Boolean,
        hardIncluded: Boolean,
        limit: Int,
        offset: Int
    ): List<Folder>


    @Query(value = "SELECT * FROM Folder LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    fun getFolders(limit: Int, offset: Int): List<Folder>

    @Query(
        value = "Select count(folder.*) from folder " +
                "   join wg_user wu on wu.id = folder.user_id " +
                "WHERE (folder_name like '%' || ?1 || '%') " +
                "    and (folder.category like '%' || ?2 || '%') " +
                "    and (nullif(?3, -1 ) is null or wu.id = ?3) " +
                "    and (?4 = 0 " +
                "   or (float8(folder.up_votes) / nullif(folder.up_votes + folder.down_votes, 0)) > ?4) " +
                "    and ((not (?5 or ?6 or ?7)) " +
                "   or (?5 and difficulty = 0) " +
                "   or (?6 and difficulty = 1) " +
                "   or (?7 and difficulty = 2)) ", nativeQuery = true
    )
    fun getFolderCnt(
        namePart: String,
        categoryPart: String,
        userId: Long,
        minimalUpvotePercentage: Float,
        easyIncluded: Boolean,
        mediumIncluded: Boolean,
        hardIncluded: Boolean
    ): Int

    @Query("SELECT votes.is_upvote FROM votes where from_user = ?1 and on_pack is null and  on_folder = ?2 ", nativeQuery = true)
    fun getVoteOnFolder(fromId: Long, onId: Long): List<Boolean>

}
