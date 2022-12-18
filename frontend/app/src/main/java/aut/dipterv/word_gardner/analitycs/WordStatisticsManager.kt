package aut.dipterv.word_gardner.analitycs

import android.os.SystemClock
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.local_data.models.Statistics
import aut.dipterv.word_gardner.network_data.models.RawWordStatistics
import aut.dipterv.word_gardner.network_data.models.dtos.UserStatisticsDto
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.HintType
import aut.dipterv.word_gardner.ui.practice.screens.practice.utils.enums.InputType

class WordStatisticsManager(val dao: WordPackDao) {
    var timerStartedAt: Long = 0L


    companion object {
        const val LONGEST_INTERESTING_INTERVAL = 60000f
        const val RECALL_STATISTICS_PERCENTAGE = 0.5f
        const val SUCCESS_AVERAGE_PERCENTAGE = 0.25f
        const val VOTE_RATIO_PERCENTAGE = 0.25f
    }

    fun startTimer() {
        timerStartedAt = SystemClock.elapsedRealtime()
    }

    fun resetTimer(): Long {
        val presentTime = SystemClock.elapsedRealtime()
        val toReturn = presentTime - timerStartedAt
        timerStartedAt = presentTime
        return toReturn
    }

    suspend fun getRawStatistics(
        foreignWord: String, testIdentifier: TestIdentifier, succeeded: Boolean
    ): RawWordStatistics? {
        val time = resetTimer()
        val normalizedTime = time.toFloat() / LONGEST_INTERESTING_INTERVAL
        if (normalizedTime <= 1) {
            val identifierInt = testIdentifier.toInt()
            val localStatistics = dao.getStatistics(identifierInt, foreignWord)
            var succeededCnt = 0
            var succeededTimeAverage = 0f
            var faultedCnt = 0
            var faultedTimeAverage = 0f
            if (localStatistics.isNotEmpty()) {
                succeededCnt = localStatistics.first().succeeded
                succeededTimeAverage = localStatistics.first().succeededTimeAverage
                faultedCnt = localStatistics.first().faulted
                faultedTimeAverage = localStatistics.first().faultedTimeAverage
            }
            if (succeeded) {
                succeededTimeAverage =
                    (succeededTimeAverage * succeededCnt + (time / LONGEST_INTERESTING_INTERVAL))
                succeededTimeAverage /= ++succeededCnt
            } else {
                faultedTimeAverage =
                    (faultedTimeAverage * faultedCnt + (time / LONGEST_INTERESTING_INTERVAL))
                faultedTimeAverage /= ++faultedCnt
            }

            if (localStatistics.isNotEmpty()) {
                dao.updateStatistics(
                    id = localStatistics.first().id,
                    succeeded = succeededCnt,
                    succeededTimeAverage = succeededTimeAverage,
                    faulted = faultedCnt,
                    faultedTimeAverage = faultedTimeAverage
                )
            } else {
                dao.insertStatistics(
                    Statistics(
                        testIdentifier = identifierInt,
                        foreignWord = foreignWord,
                        succeeded = succeededCnt,
                        succeededTimeAverage = succeededTimeAverage,
                        faulted = faultedCnt,
                        faultedTimeAverage = faultedTimeAverage
                    )
                )
            }

            return RawWordStatistics(
                testIdentifier = identifierInt,
                succeeded = succeeded,
                time = normalizedTime,
                foreignWord = foreignWord
            )
        }
        return null
    }

    suspend fun getRecommendationsFrontend(
        hints: List<HintType>,
        inputs: List<InputType>
    ): HashMap<Int, Float> {
        val statistics = dao.getStatistics()
        val hashMap: HashMap<Int, Float> = getBase(hints, inputs, 0f)

        val denHashMap = HashMap<Int, Float>()

        hashMap.forEach {
            denHashMap[it.key] = 0f
        }

        statistics.forEach {
            if (hashMap.containsKey(it.testIdentifier)) {
                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier] ?: 0f) +
                            ((it.succeeded).toFloat() / (it.succeeded + it.faulted)) * VOTE_RATIO_PERCENTAGE

                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier] ?: 0f) +
                            ((it.succeededTimeAverage * it.succeeded)
                                    / (it.succeededTimeAverage * it.succeeded
                                    + it.faultedTimeAverage * it.faulted)) * SUCCESS_AVERAGE_PERCENTAGE


                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier] ?: 0f) +
                            ((it.succeeded.toFloat() - (it.succeeded * it.succeededTimeAverage))
                                    / (it.succeeded + it.faulted - it.succeededTimeAverage * it.succeeded
                                    - it.faultedTimeAverage * it.faulted)) * RECALL_STATISTICS_PERCENTAGE

                denHashMap[it.testIdentifier] =
                    (denHashMap[it.testIdentifier] ?: 0f) + it.succeeded + it.faulted

            }
        }

        hashMap.forEach {
            hashMap[it.key] = (it.value) / if ((denHashMap[it.key] ?: 1f).toInt() == 0) {
                1f
            } else {
                (denHashMap[it.key] ?: 1f)
            }
            hashMap[it.key] = it.value + 0.5f
        }

        return hashMap
    }

    fun getRecommendationsBackend(
        hints: List<HintType>,
        inputs: List<InputType>,
        userStatisticsDtos: List<UserStatisticsDto>
    ): HashMap<Int, Float> {
        val hashMap = getBase(hints, inputs, 0.5f)

        userStatisticsDtos.forEach {
            if (hashMap.containsKey(it.testIdentifier)) {
                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier] ?: 0f) + it.voteRatio * VOTE_RATIO_PERCENTAGE

                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier]
                        ?: 0f) + it.succeedTimeAverage * SUCCESS_AVERAGE_PERCENTAGE

                hashMap[it.testIdentifier] =
                    (hashMap[it.testIdentifier]
                        ?: 0f) + it.recallScore * RECALL_STATISTICS_PERCENTAGE
            }
        }
        return hashMap
    }

    private fun getBase(
        hints: List<HintType>,
        inputs: List<InputType>,
        basePercentage: Float
    ): HashMap<Int, Float> {
        val identifiers = ArrayList<Int>()
        val hashMap = HashMap<Int, Float>()

        hints.forEach { hint ->
            inputs.forEach { input ->
                identifiers.addAll(TestIdentifier.getIdentifierInts(hint, input))
            }
        }

        identifiers.forEach { hashMap[it] = basePercentage }

        return hashMap
    }

}
