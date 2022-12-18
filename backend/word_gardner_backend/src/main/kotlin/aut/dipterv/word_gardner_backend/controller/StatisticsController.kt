package aut.dipterv.word_gardner_backend.controller

import aut.dipterv.word_gardner_backend.dto.UserStatisticsDto
import aut.dipterv.word_gardner_backend.model.RawWordStatistics
import aut.dipterv.word_gardner_backend.model.Statistics
import aut.dipterv.word_gardner_backend.service.StatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Validated
@RequestMapping("\${api.base-path:/api}")
class StatisticsController {
    @Autowired
    private lateinit var statisTicsService: StatisticsService

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/data/statistics"]
    )
    fun createStatistics(
        @Valid @RequestBody body: Statistics
    ): ResponseEntity<Long> {
        val id = statisTicsService.createStatistics(body)

        return ResponseEntity(id, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.POST],
            value = ["/data/word_statistics"]
    )
    fun createRawWordStatistics(
        @Valid @RequestBody body: RawWordStatistics,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        val id = statisTicsService.createRawWordStatistics(body, token)

        return ResponseEntity(id, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/word_statistics/calc"]
    )
    fun packWordStatistics(
    ): ResponseEntity<Unit> {
        statisTicsService.packWordStatistics()

        return ResponseEntity(Unit, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/statistics"]
    )
    fun getStatistics(
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<List<UserStatisticsDto>> {
        val statistics = statisTicsService.exportWordStatistics(token)

        return ResponseEntity(statistics, HttpStatus.OK)
    }
}
