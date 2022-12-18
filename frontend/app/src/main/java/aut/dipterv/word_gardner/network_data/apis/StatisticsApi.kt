package aut.dipterv.word_gardner.network_data.apis

import aut.dipterv.word_gardner.network_data.models.RawWordStatistics
import aut.dipterv.word_gardner.network_data.models.StatisticsModel
import aut.dipterv.word_gardner.network_data.models.dtos.UserStatisticsDto
import io.reactivex.Single
import retrofit2.http.*

interface StatisticsApi {

    companion object {
        private const val BASE_PATH = "data"
        const val STATISTICS = "$BASE_PATH/statistics"
        const val WORD_STATISTICS = "$BASE_PATH/word_statistics"
        const val BY_USER = "$STATISTICS/{user_id}"
    }

    @PUT(STATISTICS)
    fun addStatistics(
        @Body statistics: StatisticsModel
    ): Single<Long>

    @GET(BY_USER)
    fun getStatisticsByUser(
        @Path("user_id") userId: Long
    ): Single<StatisticsModel>

    @POST(WORD_STATISTICS)
    fun addRawWordStatistics(
        @Body rawWordStatistics: RawWordStatistics
    ): Single<Long>

    @GET(STATISTICS)
    fun getStatistics(
    ): Single<List<UserStatisticsDto>>
}