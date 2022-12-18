package aut.dipterv.word_gardner_backend.controller

import aut.dipterv.word_gardner_backend.dto.VotesDto
import aut.dipterv.word_gardner_backend.model.Vote
import aut.dipterv.word_gardner_backend.service.VotesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("\${api.base-path:/api}")
class VotesController {

    @Autowired
    private lateinit var votesService: VotesService

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/data/vote"],
        produces = ["application/json"]
    )
    fun createVote(
        @Valid @RequestBody body: Vote,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        return ResponseEntity(votesService.saveVote(body, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/vote/user/{id}"],
        produces = ["application/json"]
    )
    fun getVoteByUser(
        @PathVariable(name = "id") id: Long,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Vote> {
        return ResponseEntity(votesService.getVoteByUser(id, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/vote/folder/{id}"],
        produces = ["application/json"]
    )
    fun getVoteByFolder(
        @PathVariable(name = "id") id: Long,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Vote> {
        return ResponseEntity(votesService.getVoteByFolder(id, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/vote/pack/{id}"],
        produces = ["application/json"]
    )
    fun getVoteByPack(
        @PathVariable(name = "id") id: Long,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Vote> {
        return ResponseEntity(votesService.getVoteByPack(id, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/votes/user/{id}"],
        produces = ["application/json"]
    )
    fun getVotesForUser(
        @PathVariable(name = "id") id: Long
    ): ResponseEntity<VotesDto> {
        return ResponseEntity(votesService.getVotesForUser(id), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/votes/pack/{id}"],
        produces = ["application/json"]
    )
    fun getVotesForPack(
        @PathVariable(name = "id") id: Long
    ): ResponseEntity<VotesDto> {
        return ResponseEntity(votesService.getVotesForPack(id), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/votes/folder/{id}"],
        produces = ["application/json"]
    )
    fun getVotesForFolder(
        @PathVariable(name = "id") id: Long
    ): ResponseEntity<VotesDto> {
        return ResponseEntity(votesService.getVotesForFolder(id), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/vote/delete"],
        produces = ["application/json"]
    )
    fun deleteVote(
        @Valid @RequestBody body: Vote,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Unit> {
        votesService.deleteVote(body, token)
        return ResponseEntity(HttpStatus.OK)
    }

}