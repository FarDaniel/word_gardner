package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.Vote
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VotesRepository : CrudRepository<Vote, Long> {

    @Query(value = "SELECT count(*) FROM Vote WHERE to_user = ?1 AND is_upvote ", nativeQuery = true)
    fun getUpVotesOfUser(userId: Long): Int

    @Query(value = "SELECT count(*) FROM Vote WHERE on_folder = ?1 AND is_upvote ", nativeQuery = true)
    fun getUpVotesOfFolder(folderId: Long): Int

    @Query(value = "SELECT count(*) FROM Vote WHERE on_pack= ?1 AND is_upvote ", nativeQuery = true)
    fun getUpVotesOfPack(packId: Long): Int


    @Query(value = "SELECT count(*) FROM Vote WHERE to_user = ?1 AND NOT is_upvote ", nativeQuery = true)
    fun getDownVotesOfUser(userId: Long): Int

    @Query(value = "SELECT count(*) FROM Vote WHERE on_folder = ?1 AND NOT is_upvote ", nativeQuery = true)
    fun getDownVotesOfFolder(folderId: Long): Int

    @Query(value = "SELECT count(*) FROM Vote WHERE on_pack= ?1 AND NOT is_upvote ", nativeQuery = true)
    fun getDownVotesOfPack(packId: Long): Int


    @Query(value = "SELECT * FROM Vote WHERE from_user = ?1 AND to_user = ?2 AND on_pack ISNULL AND on_folder ISNULL ", nativeQuery = true)
    fun getVoteByUser(fromUser: Long, toUser: Long): List<Vote>

    @Query(value = "SELECT * FROM Vote WHERE from_user = ?1 AND on_folder = ?2 ", nativeQuery = true)
    fun getVoteByFolder(fromUser: Long, folderId: Long): List<Vote>

    @Query(value = "SELECT * FROM Vote WHERE from_user = ?1 AND on_pack = ?2 ", nativeQuery = true)
    fun getVoteByPack(fromUser: Long, packId: Long): List<Vote>

    @Query(value = "SELECT * FROM vote WHERE from_user = ?1 AND to_user = ?2 AND (( ?3 isnull and on_pack isnull) or ( ?3 notnull and on_pack = ?3 )) AND (( ?4 isnull and on_folder isnull) or ( ?4 notnull and on_folder = ?4 )) ", nativeQuery = true)
    fun selectVote(fromUser: Long, toUser: Long, packId: Long?, folderId: Long?): List<Vote>

}
