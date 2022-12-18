package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.Pack
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PackRepository : CrudRepository<Pack, Long> {

    @Query(
        value = "Select pack.* from pack " +
                "   join wg_user wu on wu.id = pack.user_id " +
                "WHERE (?1 is null or pack_name like '%' || ?1 || '%') " +
                "    and (?2 is null or pack.category like '%' || ?2 || '%') " +
                "    and (nullif(?3, -1 ) is null or wu.id = ?3) " +
                "    and (?4 = 0 " +
                "   or (float8(pack.up_votes) / nullif(pack.up_votes + pack.down_votes, 0)) > ?4) " +
                "    and ((not (?5 or ?6 or ?7)) " +
                "   or (?5 and difficulty = 0) " +
                "   or (?6 and difficulty = 1) " +
                "   or (?7 and difficulty = 2)) " +
                "ORDER BY pack.up_votes / coalesce(nullif((pack.up_votes + pack.down_votes), 0), 1) desc, pack.up_votes desc, pack.id desc " +
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
    ): List<Pack>

    @Query(value = "SELECT * FROM Pack LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    fun getPacks(limit: Int, offset: Int): List<Pack>


    @Query(
        value = "Select count(pack.*) from pack " +
                "   join wg_user wu on wu.id = pack.user_id " +
                "WHERE (pack_name like '%' || ?1 || '%') " +
                "    and (pack.category like '%' || ?2 || '%') " +
                "    and (nullif(?3, -1 ) is null or wu.id = ?3) " +
                "    and (?4 = 0 " +
                "   or (float8(pack.up_votes) / nullif(pack.up_votes + pack.down_votes, 0)) > ?4) " +
                "    and ((not (?5 or ?6 or ?7)) " +
                "   or (?5 and difficulty = 0) " +
                "   or (?6 and difficulty = 1) " +
                "   or (?7 and difficulty = 2)) ", nativeQuery = true
    )
    fun getPackCnt(
        namePart: String,
        categoryPart: String,
        userId: Long,
        minimalUpvotePercentage: Float,
        easyIncluded: Boolean,
        mediumIncluded: Boolean,
        hardIncluded: Boolean
    ): Int

    @Query(
        value = "Select count(distinct pack.id) " +
                "from pack " +
                "         join pack_folder_cross_ref pfcr on pack.id = pfcr.pack_id " +
                "WHERE pfcr.folder_id = ?1 ", nativeQuery = true
    )
    fun getPackCntByFolder(folderId: Long): Int

    @Query(
        value = "SELECT distinct pack.* " +
                "FROM pack " +
                "         JOIN pack_folder_cross_ref pfcr on pack.id = pfcr.pack_id " +
                "WHERE pfcr.folder_id = ?1 " +
                "LIMIT ?2 OFFSET ?3 ", nativeQuery = true
    )
    fun getPacksByFolder(folderId: Long, limit: Int, offset: Int): List<Pack>

    @Query("SELECT votes.is_upvote FROM votes where from_user = ?1 and on_folder is null and  on_pack = ?2 ", nativeQuery = true)
    fun getVoteOnPack(fromId: Long, onId: Long): List<Boolean>

}
