package aut.dipterv.word_gardner_backend.controller

import aut.dipterv.word_gardner_backend.dto.CountDto
import aut.dipterv.word_gardner_backend.dto.SearchFilter
import aut.dipterv.word_gardner_backend.dto.model_dto.FolderDto
import aut.dipterv.word_gardner_backend.dto.model_dto.PackDto
import aut.dipterv.word_gardner_backend.model.*
import aut.dipterv.word_gardner_backend.repository.*
import aut.dipterv.word_gardner_backend.service.CardsService
import aut.dipterv.word_gardner_backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("\${api.base-path:/api}")
class CardsController {

    @Autowired
    private lateinit var cardsService: CardsService

    @Autowired
    private lateinit var userService: UserService

    @RequestMapping(
        method = [RequestMethod.POST], value = ["/data/pack_folder"]
    )
    @ResponseBody
    fun addPackFolderCrossRef(
        @RequestBody packFolderCrossRef: PackFolderCrossRef, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        val id = cardsService.addPackFolderCrossRef(packFolderCrossRef, token)

        return ResponseEntity(id, HttpStatus.CREATED)
    }

    @RequestMapping(
        method = [RequestMethod.POST], value = ["/data/folder"], consumes = ["application/json"]
    )
    fun createFolder(
        @Valid @RequestBody body: Folder, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        val id = cardsService.createFolder(body, token)

        return ResponseEntity(id, HttpStatus.CREATED)
    }

    @RequestMapping(
        method = [RequestMethod.POST], value = ["/data/pack"], consumes = ["application/json"]
    )
    fun createPack(
        @Valid @RequestBody body: Pack, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        return ResponseEntity(cardsService.createPack(body, token), HttpStatus.CREATED)
    }

    @RequestMapping(
        method = [RequestMethod.POST], value = ["/data/card"], consumes = ["application/json"]
    )
    fun createCard(
        @Valid @RequestBody body: WordCard, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Long> {
        return ResponseEntity(cardsService.createCard(body, token), HttpStatus.CREATED)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/pack/{limit}/{offset}"], produces = ["application/json"]
    )
    fun getPacks(
        @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int
    ): ResponseEntity<List<Pack>> {
        val response = ArrayList<Pack>()

        response.addAll(cardsService.getPacks(limit, offset))

        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/card/{packId}/{limit}/{offset}"], produces = ["application/json"]
    )
    fun getWordCards(
        @PathVariable("packId") packId: Long, @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int
    ): ResponseEntity<List<WordCard>> {
        val response = ArrayList<WordCard>()
        response.addAll(cardsService.getWordCards(packId, limit, offset))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/card/count/{packId}"], produces = ["application/json"]
    )
    fun getWordCardCnt(
        @PathVariable("packId") packId: Long
    ): ResponseEntity<CountDto> {
        return ResponseEntity(cardsService.getWordCardCnt(packId), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/folder/{Id}"], produces = ["application/json"]
    )
    fun getFolder(
        @PathVariable("Id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<FolderDto> {
        return ResponseEntity(cardsService.getFolder(id, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/folder/filtered/{limit}/{offset}"],
        produces = ["application/json"]
    )
    fun getFilteredFolders(
        @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int, @Valid @RequestBody body: SearchFilter
    ): ResponseEntity<List<Folder>> {
        val response = ArrayList<Folder>()

        response.addAll(cardsService.getFilteredFolders(limit, offset, body))

        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/pack/{id}"], produces = ["application/json"]
    )
    fun getPack(
        @PathVariable("id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<PackDto> {
        return ResponseEntity(cardsService.getPack(id, token), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH], value = ["/data/pack/count"], produces = ["application/json"]
    )
    fun getPackCnt(
        @Valid @RequestBody body: SearchFilter
    ): ResponseEntity<CountDto> {
        return ResponseEntity(
            cardsService.getPackCnt(
                body
            ), HttpStatus.OK
        )
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/pack/count/{folderId}"], produces = ["application/json"]
    )
    fun getPackCntByFolder(
        @PathVariable("folderId") id: Long
    ): ResponseEntity<CountDto> {
        return ResponseEntity(cardsService.getPackCntByFolder(id), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/pack/{folderId}/{limit}/{offset}"],
        produces = ["application/json"]
    )
    fun getPacksByFolder(
        @PathVariable("folderId") id: Long, @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int
    ): ResponseEntity<List<Pack>> {
        val response = ArrayList<Pack>()
        response.addAll(cardsService.getPacksByFolder(id, limit, offset))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.GET], value = ["/data/folder/{limit}/{offset}"], produces = ["application/json"]
    )
    fun getFolders(
        @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int
    ): ResponseEntity<List<Folder>> {
        val response = ArrayList<Folder>()
        response.addAll(cardsService.getFolders(limit, offset))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PUT], value = ["/data/folder"], consumes = ["application/json"]
    )
    fun updateFolder(
        @Valid @RequestBody body: FolderDto, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<String> {
        cardsService.updateFolder(body, userService.getUserIdFromJwtToken(token))
        return ResponseEntity("Updated", HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PUT], value = ["/data/pack"], consumes = ["application/json"]
    )
    fun updatePack(
        @Valid @RequestBody body: PackDto, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<String> {
        cardsService.updatePack(body, userService.getUserIdFromJwtToken(token))
        return ResponseEntity("Updated", HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.DELETE], value = ["/packFolderCrossRef/{id}"]
    )
    @ResponseBody
    fun deletePackFolderCrossRef(
        @PathVariable("id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<String> {
        val succeeded = cardsService.deletePackFolderCrossRef(id, token)

        return if (succeeded) ResponseEntity("Deleted", HttpStatus.OK)
        else ResponseEntity("Didn't exist", HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.DELETE], value = ["/data/folder/{id}"]
    )
    fun deleteFolder(
        @PathVariable("id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Boolean> {
        println("T0rl;s t0rt;n;s van's$")
        val succeeded = cardsService.deleteFolder(id, token)

        return ResponseEntity(succeeded, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.DELETE], value = ["/data/pack/{Id}"]
    )
    fun deletePack(
        @PathVariable("Id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Boolean> {
        println("törlés hívva lett")
        val succeeded = cardsService.deletePack(id, token)

        return ResponseEntity(succeeded, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.DELETE], value = ["/data/card/{id}"]
    )
    fun deleteCard(
        @PathVariable("id") id: Long, @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Boolean> {
        val succeeded = cardsService.deleteCard(id, token)

        return ResponseEntity(succeeded, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH], value = ["/data/folder/count"], produces = ["application/json"]
    )
    fun getFolderCnt(
        @Valid @RequestBody body: SearchFilter
    ): ResponseEntity<CountDto> {
        return ResponseEntity(
            cardsService.getFolderCnt(
                body
            ), HttpStatus.OK
        )
    }


    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/pack/filtered/{limit}/{offset}"],
        produces = ["application/json"]
    )
    fun getFilteredPacks(
        @PathVariable("limit") limit: Int, @PathVariable("offset") offset: Int, @Valid @RequestBody body: SearchFilter
    ): ResponseEntity<List<Pack>> {
        val response = ArrayList<Pack>()

        response.addAll(cardsService.getFilteredPacks(limit, offset, body))

        return ResponseEntity(response, HttpStatus.OK)
    }

}
