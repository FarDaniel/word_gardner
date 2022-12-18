package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.WgUser
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<WgUser, Long> {

    @Query(
        value = "select distinct u.*, u.up_votes / coalesce(nullif((u.up_votes + u.down_votes), 0), 1) as upvote_percentage " +
                "from wg_user u " +
                "join pack p on u.id = p.user_id " +
                "where (nullif(?1, '') is null or name like '%' || nullif(?1, '') || '%') " +
                "    and (?2 = 0 " +
                "        or (float8(u.up_votes) / nullif(u.up_votes + u.down_votes, 0)) > ?2) " +
                "    and ((not (?3 or ?4 or ?5)) " +
                "   or (?3 and p.difficulty = 0) " +
                "   or (?4 and p.difficulty = 1) " +
                "   or (?5 and p.difficulty = 2)) " +
                "ORDER BY upvote_percentage desc, u.up_votes desc, u.id desc " +
                "limit ?6 OFFSET ?7 ", nativeQuery = true
    )
    fun getFiltered(
        namePart: String,
        minimalUpvotePercentage: Float,
        easyIncluded: Boolean,
        mediumIncluded: Boolean,
        hardIncluded: Boolean,
        limit: Int,
        offset: Int
    ): List<WgUser>

    @Query(value = "SELECT * FROM wg_user LIMIT ?1 OFFSET ?2 ", nativeQuery = true)
    fun getUsers(limit: Int, offset: Int): List<WgUser>

    @Query(value = "SELECT * FROM wg_user WHERE name = ?1 LIMIT 1 ", nativeQuery = true)
    fun getUserByName(userName: String): List<WgUser>

    @Query(value = "select count(distinct  u.id) " +
                "from wg_user u " +
                "join pack p on u.id = p.user_id " +
                "where (nullif(?1, '') is null or name like '%' || nullif(?1, '') || '%') " +
                "    and (?2 = 0 " +
                "        or (float8(u.up_votes) / nullif(u.up_votes + u.down_votes, 0)) > ?2) " +
                "    and ((not (?3 or ?4 or ?5)) " +
                "   or (?3 and p.difficulty = 0) " +
                "   or (?4 and p.difficulty = 1) " +
                "   or (?5 and p.difficulty = 2)) ", nativeQuery = true)
    fun getUserCnt(
        namePart: String,
        minimalUpvotePercentage: Float,
        easyIncluded: Boolean,
        mediumIncluded: Boolean,
        hardIncluded: Boolean
    ): Int

    @Query("SELECT count(*) FROM votes where to_user = ?1 and is_upvote = true ", nativeQuery = true)
    fun getUpVotes(id: Long): Int

    @Query("SELECT count(*) FROM votes where to_user = ?1 and is_upvote = false ", nativeQuery = true)
    fun getDownVotes(id: Long): Int

}
