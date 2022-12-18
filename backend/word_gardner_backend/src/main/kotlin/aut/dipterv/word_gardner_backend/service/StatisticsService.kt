package aut.dipterv.word_gardner_backend.service

import aut.dipterv.word_gardner_backend.dto.UserStatisticsDto
import aut.dipterv.word_gardner_backend.model.RawWordStatistics
import aut.dipterv.word_gardner_backend.model.Statistics
import aut.dipterv.word_gardner_backend.model.WordStatistics
import aut.dipterv.word_gardner_backend.repository.RawWordStatisticsRepository
import aut.dipterv.word_gardner_backend.repository.StatisticsRepository
import aut.dipterv.word_gardner_backend.repository.WordStatisticsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StatisticsService {

    @Autowired
    private lateinit var statisticsRepository: StatisticsRepository

    @Autowired
    private lateinit var wordStatisticsRepository: WordStatisticsRepository

    @Autowired
    private lateinit var rawWordStatisticsRepository: RawWordStatisticsRepository

    @Autowired
    private lateinit var userService: UserService

    fun createStatistics(statistics: Statistics): Long {
        val current = statisticsRepository.getStatisticks(statistics.userId, statistics.testIdentifier)
        return if (current.isEmpty()) {
            statisticsRepository.save(statistics).id
        } else {
            current.first().id
        }
    }

    fun createRawWordStatistics(rawWordStatistics: RawWordStatistics, token: String): Long {
        var finalStatistics: RawWordStatistics = rawWordStatistics
        if (!statisticsRepository.existsById(rawWordStatistics.statisticsId)) {
            val userId = userService.getUserIdFromJwtToken(token)
            val statisticsId = createStatistics(
                Statistics(
                    -1,
                    userId,
                    rawWordStatistics.testIdentifier
                )
            )
            finalStatistics = RawWordStatistics(
                id = rawWordStatistics.id,
                statisticsId = statisticsId,
                testIdentifier = rawWordStatistics.testIdentifier,
                succeeded = rawWordStatistics.succeeded,
                time = rawWordStatistics.time,
                foreignWord = rawWordStatistics.foreignWord
            )
        }
        return rawWordStatisticsRepository.save(finalStatistics).id
    }

    fun updateWordStatistics(raws: List<RawWordStatistics>): WordStatistics? {
        if (raws.isNotEmpty()) {
            val statisticsId = raws.first().statisticsId
            val foreignWord = raws.first().foreignWord
            var wordStatistics = wordStatisticsRepository.getWordStatistics(statisticsId, foreignWord)

            var success = 0
            var fault = 0
            var sumSuccessTime = 0f
            var sumFaultTime = 0f

            if (wordStatistics.isNotEmpty()) {
                success = wordStatistics.first().succeeded
                fault = wordStatistics.first().faulted
                sumSuccessTime = wordStatistics.first().succeedTimeAverage * success
                sumFaultTime = wordStatistics.first().faultedTimeAverage * fault
            }

            raws.forEach {
                if (it.succeeded) {
                    success++
                    sumSuccessTime += it.time
                } else {
                    fault++
                    sumFaultTime += it.time
                }
            }
            val newStatistics =
                WordStatistics(
                    id = if(wordStatistics.isEmpty()){0} else {wordStatistics.first().id},
                    statisticsId = statisticsId,
                    succeeded = success,
                    succeedTimeAverage = if (success == 0) {
                        0f
                    } else {
                        sumSuccessTime / success
                    },
                    faulted = fault,
                    faultedTimeAverage = if (fault == 0) {
                        0f
                    } else {
                        sumFaultTime / fault
                    },
                    foreignWord = foreignWord,
                )
            wordStatisticsRepository.save(
                newStatistics
            )
            return newStatistics
        }
        return null
    }

    fun packWordStatistics() {
        val raws = ArrayList<RawWordStatistics>()
        val map = HashMap<Pair<Long, String>, ArrayList<RawWordStatistics>>()

        raws.addAll(rawWordStatisticsRepository.getRawWordStatistics(1000))

        while (raws.size != 0) {
            raws.forEach { raw ->
                val pair = Pair(raw.statisticsId, raw.foreignWord)
                if (!map.containsKey(pair)) {
                    map[pair] = ArrayList()
                }
                map[pair]?.add(raw)
                rawWordStatisticsRepository.delete(raw)
            }
            map.forEach { entry ->
                updateWordStatistics(entry.value)
            }
            map.clear()
            raws.clear()
            raws.addAll(rawWordStatisticsRepository.getRawWordStatistics(1000))
        }
    }

    fun exportWordStatistics(token: String): List<UserStatisticsDto> {
        val userId = userService.getUserIdFromJwtToken(token)
        val outStatistics = ArrayList<UserStatisticsDto>()

        val raws = ArrayList<RawWordStatistics>()
        val stats = statisticsRepository.getStatisticks(userId)
        val rawMap = HashMap<String, ArrayList<RawWordStatistics>>()
        val exportedMap = HashMap<String, WordStatistics>()

        stats.forEach {
            val exported = wordStatisticsRepository.getWordStatistics(it.id)
            var voteCnt = 0
            var succeededCnt = 0
            var allSucceededTime = 0f
            var allFaultedTime = 0f
            raws.addAll(rawWordStatisticsRepository.getRawWordStatistics(it.id))
            exported.forEach { newExported ->
                val word = newExported.foreignWord
                exportedMap[word] = newExported
            }
            raws.forEach { raw ->
                val word = raw.foreignWord
                if (!rawMap.containsKey(word)) {
                    rawMap[word] = ArrayList()
                }
                rawMap[word]?.add(raw)
                rawWordStatisticsRepository.delete(raw)
            }
            rawMap.forEach { entry ->
                exportedMap.remove(entry.key)
                val wordStatistics = updateWordStatistics(entry.value)
                voteCnt += (wordStatistics?.faulted ?: 0) + (wordStatistics?.succeeded ?: 0)
                succeededCnt += wordStatistics?.succeeded ?: 0
                allSucceededTime += (wordStatistics?.succeedTimeAverage ?: 0f) * (wordStatistics?.succeeded ?: 0)
                allFaultedTime += (wordStatistics?.faultedTimeAverage ?: 0f) * (wordStatistics?.faulted ?: 0)
            }

            exportedMap.forEach { entry ->
                val wordStatistics = entry.value
                voteCnt += (wordStatistics.faulted) + (wordStatistics.succeeded)
                succeededCnt += wordStatistics.succeeded
                allSucceededTime += (wordStatistics.succeedTimeAverage) * (wordStatistics.succeeded)
                allFaultedTime += (wordStatistics.faultedTimeAverage) * (wordStatistics.faulted)
            }
                val userStatisticsFromExported =
                    calculateUserStatisticsDto(
                        voteCnt,
                        succeededCnt,
                        allSucceededTime,
                        allFaultedTime,
                        it.testIdentifier
                    )
                outStatistics.add(userStatisticsFromExported)
            exportedMap.clear()
            rawMap.clear()
            raws.clear()
        }
        return outStatistics
    }

    private fun calculateUserStatisticsDto(
        voteCnt: Int,
        succeededCnt: Int,
        allSucceededTime: Float,
        allFaultedTime: Float,
        testIdentifier: Int
    ): UserStatisticsDto {
        val allTimeScore = (voteCnt - allSucceededTime - allFaultedTime)
        return UserStatisticsDto(
            testIdentifier,
            if (succeededCnt == 0) {
                0f
            } else {
                Int
                allSucceededTime / succeededCnt
            },
            if (voteCnt == 0) {
                0f
            } else {
                succeededCnt.toFloat() / voteCnt
            },
            if (allTimeScore == 0f) {
                0f
            } else {
                (succeededCnt - (allSucceededTime)) / allTimeScore
            }
        )
    }

}
