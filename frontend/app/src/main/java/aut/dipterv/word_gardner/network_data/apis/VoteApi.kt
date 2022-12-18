package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.VoteModel
import aut.dipterv.word_gardner.network_data.models.dtos.VotesDto
import io.reactivex.Single
import retrofit2.http.*

interface VoteApi {

    companion object {
        private const val BASE_PATH = "data"
        const val SINGLE_VOTE = "$BASE_PATH/vote"
        const val DELETE = "$SINGLE_VOTE/delete"
        const val VOTES = "$BASE_PATH/votes"
        const val SINGLE_BY_USER = "$SINGLE_VOTE/user/{id}"
        const val SINGLE_BY_PACK = "$SINGLE_VOTE/pack/{id}"
        const val SINGLE_BY_FOLDER = "$SINGLE_VOTE/folder/{id}"
        const val BY_USER = "$VOTES/user/{id}"
        const val BY_PACK = "$VOTES/pack/{id}"
        const val BY_FOLDER = "$VOTES/folder/{id}"
    }

    @POST(SINGLE_VOTE)
    fun addVote(
        @Body vote: VoteModel
    ): Single<Long>

    @GET(SINGLE_BY_USER)
    fun getVoteByUser(
        @Path("id") userId: Long
    ): Single<VoteModel>

    @GET(SINGLE_BY_PACK)
    fun getVoteByPack(
        @Path("id") packId: Long
    ): Single<VoteModel>

    @GET(SINGLE_BY_FOLDER)
    fun getVoteByFolder(
        @Path("id") folderId: Long
    ): Single<VoteModel>

    @GET(BY_USER)
    fun getVotesByUser(
        @Path("id") userId: Long
    ): Single<VotesDto>

    @GET(BY_PACK)
    fun getVotesByPack(
        @Path("id") packId: Long
    ): Single<VotesDto>

    @GET(BY_FOLDER)
    fun getVotesByFolder(
        @Path("id") folderId: Long
    ): Single<VotesDto>

    @PATCH(DELETE)
    fun deleteVote(
        @Body vote: VoteModel
    ): Single<Unit>

}