package aut.dipterv.word_gardner.network_data.interactors

import aut.dipterv.word_gardner.network_data.apis.StatisticsApi
import aut.dipterv.word_gardner.network_data.models.RawWordStatistics
import aut.dipterv.word_gardner.network_data.models.StatisticsModel
import aut.dipterv.word_gardner.network_data.models.dtos.UserStatisticsDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StatisticsInteractor(private val statisticsApi: StatisticsApi) {

    fun addStatistics(statistics: StatisticsModel): Single<Long> {
        return statisticsApi.addStatistics(statistics)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStatisticsByUser(userId: Long): Single<StatisticsModel> {
        return statisticsApi.getStatisticsByUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addRawWordStatistics(rawWordStatistics: RawWordStatistics): Single<Long> {
        return statisticsApi.addRawWordStatistics(rawWordStatistics)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStatistics(): Single<List<UserStatisticsDto>> {
        return statisticsApi.getStatistics()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
