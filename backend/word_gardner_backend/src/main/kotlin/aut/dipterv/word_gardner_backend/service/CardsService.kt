package aut.dipterv.word_gardner_backend.service

import aut.dipterv.word_gardner_backend.dto.CountDto
import aut.dipterv.word_gardner_backend.dto.SearchFilter
import aut.dipterv.word_gardner_backend.dto.model_dto.FolderDto
import aut.dipterv.word_gardner_backend.dto.model_dto.PackDto
import aut.dipterv.word_gardner_backend.exception.AuthorizationException
import aut.dipterv.word_gardner_backend.exception.FormatViolationException
import aut.dipterv.word_gardner_backend.exception.NotFoundException
import aut.dipterv.word_gardner_backend.model.Folder
import aut.dipterv.word_gardner_backend.model.Pack
import aut.dipterv.word_gardner_backend.model.PackFolderCrossRef
import aut.dipterv.word_gardner_backend.model.WordCard
import aut.dipterv.word_gardner_backend.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CardsService {

    @Autowired
    private lateinit var packFolderCrossRefRepository: PackFolderCrossRefRepository

    @Autowired
    private lateinit var folderRepository: FolderRepository

    @Autowired
    private lateinit var packRepository: PackRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var wordCardRepository: WordCardRepository

    @Autowired
    private lateinit var userService: UserService

    companion object {
        val backGrounds = listOf(
            "gray",
            "red",
            "orange",
            "green",
            "purple",
            "blue",
            "yellow"
        )

        val difficulties = listOf(
            "easy",
            "medium",
            "hard",
            "undefined"
        )
    }

    fun addPackFolderCrossRef(packFolderCrossRef: PackFolderCrossRef, token: String): Long {
        if (!packRepository.existsById(packFolderCrossRef.pack_id)) {
            throw NotFoundException("Missing pack!")
        }
        if (!folderRepository.existsById(packFolderCrossRef.folder_id)) {
            throw NotFoundException("Missing folder!")
        }

        val userId = userService.getUserIdFromJwtToken(token)
        if (folderRepository.findById(packFolderCrossRef.folder_id).get().user_id != userId) {
            throw AuthorizationException()
        }

        packFolderCrossRef.id = null

        val crossRef = packFolderCrossRefRepository.save(packFolderCrossRef)
        return crossRef.id ?: -1
    }

    fun createFolder(folder: Folder, token: String): Long {
        if (!difficulties.contains(folder.difficulty.value))
            throw FormatViolationException("Please choose difficulty from above: $difficulties")

        folder.id = null
        folder.user_id = userService.getUserIdFromJwtToken(token)

        val insertedFolder = folderRepository.save(folder)
        return insertedFolder.id ?: -1
    }

    fun createPack(pack: Pack, token: String): Long {

        pack.id = null
        pack.user_id = userService.getUserIdFromJwtToken(token)

        val insertedPack = packRepository.save(pack)
        return insertedPack.id ?: -1
    }

    fun createCard(wordCard: WordCard, token: String): Long {
        if (!packRepository.existsById(wordCard.pack_id))
            throw NotFoundException("Missing pack!")
        if (!backGrounds.contains(wordCard.background.value))
            throw FormatViolationException("Please choose difficulty from above: $backGrounds")

        val userId = userService.getUserIdFromJwtToken(token)
        if (packRepository.findById(wordCard.pack_id).get().user_id != userId) {
            throw AuthorizationException()
        }

        wordCard.id = null

        val wordCard = wordCardRepository.save(wordCard)
        return wordCard.id ?: -1
    }

    fun getPacks(limit: Int, offset: Int): List<Pack> {
        return packRepository.getPacks(limit, offset)
    }

    fun getWordCards(packId: Long, limit: Int, offset: Int): List<WordCard> {
        if (!packRepository.existsById(packId))
            throw NotFoundException("Missing pack!")

        return wordCardRepository.getCardsByPack(packId, limit, offset)
    }

    fun getPack(id: Long, token: String): PackDto {
        val userId = userService.getUserIdFromJwtToken(token)
        if (!packRepository.existsById(id))
            throw NotFoundException("Missing pack!")
        val pack = packRepository.findById(id).get()

        return PackDto.fromPack(pack, userId == pack.user_id)
    }

    fun getFolders(limit: Int, offset: Int): List<Folder> {
        return folderRepository.getFolders(limit, offset)
    }

    fun updateFolder(body: FolderDto, userId: Long?) {
        if (!folderRepository.existsById(body.id))
            throw NotFoundException("Missing folder!")

        val old = folderRepository.findById(body.id).get()
        val new = Folder(
            id = if (userId == body.user_id) old.id else null,
            user_id = body.user_id ?: old.user_id,
            folderName = body.folderName ?: old.folderName,
            upVotes = body.upVotes ?: old.upVotes,
            downVotes = body.downVotes ?: old.downVotes,
            category = body.category ?: old.category,
            difficulty = body.difficulty ?: old.difficulty
        )

        folderRepository.save(new)
    }

    fun updatePack(body: PackDto, userId: Long?) {
        if (!packRepository.existsById(body.id))
            throw NotFoundException("Missing pack!")

        val old = packRepository.findById(body.id).get()
        val new = Pack(
            id = if (userId == body.user_id) old.id else null,
            user_id = body.user_id ?: old.user_id,
            upVotes = body.upVotes ?: old.upVotes,
            downVotes = body.downVotes ?: old.downVotes,
            packName = body.packName ?: old.packName,
            category = body.category ?: old.category,
            difficulty = body.difficulty ?: old.difficulty,
        )

        packRepository.save(new)
    }

    fun deletePackFolderCrossRef(id: Long, token: String): Boolean {
        if (!packFolderCrossRefRepository.existsById(id)) {
            return false
        }

        val packFolderCrossRef = packFolderCrossRefRepository.findById(id).get()
        val userId = userService.getUserIdFromJwtToken(token)
        if (packRepository.findById(packFolderCrossRef.pack_id).get().user_id != userId) {
            throw AuthorizationException()
        }
        if (folderRepository.findById(packFolderCrossRef.folder_id).get().user_id != userId) {
            throw AuthorizationException()
        }

        packFolderCrossRefRepository.deleteById(id)

        return true
    }

    fun deleteFolder(id: Long, token: String): Boolean {
        if (!folderRepository.existsById(id)) {
            return false
        }

        val userId = userService.getUserIdFromJwtToken(token)
        val folder = folderRepository.findById(id).get()
        if (folder.user_id != userId) {
            throw AuthorizationException()
        }

        packFolderCrossRefRepository.getByFolder(id).forEach {
            packFolderCrossRefRepository.deleteById(it.id ?: -1)
        }

        folderRepository.deleteById(id)

        return true
    }

    fun deletePack(id: Long, token: String): Boolean {
        println("Deleting")
        if (!packRepository.existsById(id)) {
            println("nem volt")
            return false
        }

        val userId = userService.getUserIdFromJwtToken(token)
        val pack = packRepository.findById(id).get()
        if (pack.user_id != userId) {
            throw AuthorizationException()
        }

        val cnt = wordCardRepository.getWordCardCnt(id)
        wordCardRepository.getCardsByPack(id, cnt, 0).forEach {
            wordCardRepository.deleteById(it.id ?: -1)
        }

        packRepository.deleteById(id)

        return true
    }

    fun deleteCard(id: Long, token: String): Boolean {
        if (!folderRepository.existsById(id)) {
            return false
        }

        val userId = userService.getUserIdFromJwtToken(token)
        val card = wordCardRepository.findById(id).get()
        val pack = packRepository.findById(card.pack_id).get()
        if (pack.user_id != userId) {
            throw AuthorizationException()
        }

        wordCardRepository.deleteById(id)

        return true
    }

    fun getFolderCnt(filter: SearchFilter): CountDto {
        return CountDto(
            folderRepository.getFolderCnt(
                filter.namePart,
                filter.categoryPart,
                filter.userId,
                filter.minimalUpvotePercentage,
                filter.easyIncluded,
                filter.mediumIncluded,
                filter.hardIncluded
            )
        )
    }

    fun getPackCnt(filter: SearchFilter): CountDto {
        return CountDto(
            packRepository.getPackCnt(
                filter.namePart,
                filter.categoryPart,
                filter.userId,
                filter.minimalUpvotePercentage,
                filter.easyIncluded,
                filter.mediumIncluded,
                filter.hardIncluded
            )
        )
    }

    fun getPackCntByFolder(folderId: Long): CountDto {
        return CountDto(packRepository.getPackCntByFolder(folderId))
    }

    fun getPacksByFolder(folderId: Long, limit: Int, offset: Int): List<Pack> {
        return packRepository.getPacksByFolder(folderId, limit, offset)
    }

    fun getFolder(id: Long, token: String): FolderDto {
        val userId = userService.getUserIdFromJwtToken(token)
        if (!folderRepository.existsById(id))
            throw NotFoundException("Missing folder!")
        val folder = folderRepository.findById(id).get()

        return FolderDto.fromFolder(folder, userId == folder.user_id)
    }

    fun getFilteredFolders(limit: Int, offset: Int, filter: SearchFilter): List<Folder> {
        return folderRepository.getFiltered(
            filter.namePart,
            filter.categoryPart,
            filter.userId,
            filter.minimalUpvotePercentage,
            filter.easyIncluded,
            filter.mediumIncluded,
            filter.hardIncluded,
            limit,
            offset
        )
    }

    fun getFilteredPacks(limit: Int, offset: Int, filter: SearchFilter): List<Pack> {
        return packRepository.getFiltered(
            filter.namePart,
            filter.categoryPart,
            filter.userId,
            filter.minimalUpvotePercentage,
            filter.easyIncluded,
            filter.mediumIncluded,
            filter.hardIncluded,
            limit,
            offset
        )
    }

    fun getWordCardCnt(packId: Long): CountDto {
        return CountDto(wordCardRepository.getWordCardCnt(packId))
    }

    fun getCardsByPack(packId: Long, limit: Int, offset: Int): List<WordCard> {
        return wordCardRepository.getCardsByPack(packId, limit, offset)
    }

}
