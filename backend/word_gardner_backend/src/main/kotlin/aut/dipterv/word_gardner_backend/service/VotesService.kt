package aut.dipterv.word_gardner_backend.service

import aut.dipterv.word_gardner_backend.dto.VotesDto
import aut.dipterv.word_gardner_backend.exception.NotFoundException
import aut.dipterv.word_gardner_backend.model.Vote
import aut.dipterv.word_gardner_backend.repository.FolderRepository
import aut.dipterv.word_gardner_backend.repository.PackRepository
import aut.dipterv.word_gardner_backend.repository.UserRepository
import aut.dipterv.word_gardner_backend.repository.VotesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VotesService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var folderRepository: FolderRepository

    @Autowired
    lateinit var packRepository: PackRepository

    @Autowired
    lateinit var votesRepository: VotesRepository

    @Autowired
    lateinit var userService: UserService

    fun getVotesForUser(userId: Long): VotesDto {
        if (!userRepository.existsById(userId)) {
            throw NotFoundException("Missing user!")
        }

        return VotesDto(
            votesRepository.getUpVotesOfUser(userId),
            votesRepository.getDownVotesOfUser(userId)
        )
    }

    fun getVotesForFolder(folderId: Long): VotesDto {
        if (!folderRepository.existsById(folderId)) {
            throw NotFoundException("Missing folder!")
        }

        return VotesDto(
            votesRepository.getUpVotesOfFolder(folderId),
            votesRepository.getDownVotesOfFolder(folderId)
        )
    }

    fun getVotesForPack(packId: Long): VotesDto {
        if (!packRepository.existsById(packId)) {
            throw NotFoundException("Missing pack!")
        }

        return VotesDto(
            votesRepository.getUpVotesOfPack(packId),
            votesRepository.getDownVotesOfPack(packId)
        )
    }

    fun getVoteByUser(toUser: Long, token: String): Vote? {
        val userId = userService.getUserIdFromJwtToken(token)

        val votes = votesRepository.getVoteByUser(userId, toUser)

        if (votes.isNotEmpty()) {
            return votes.first()
        }
        return null
    }

    fun getVoteByFolder(onFolder: Long?, token: String): Vote? {
        if (onFolder != null) {
            val userId = userService.getUserIdFromJwtToken(token)

            val votes = votesRepository.getVoteByFolder(userId, onFolder)

            if (votes.isNotEmpty()) {
                return votes.first()
            }
        }
        return null
    }

    fun getVoteByPack(onPack: Long?, token: String): Vote? {
        if (onPack != null) {
            val userId = userService.getUserIdFromJwtToken(token)

            val votes = votesRepository.getVoteByPack(userId, onPack)

            if (votes.isNotEmpty()) {
                return votes.first()
            }
        }
        return null
    }

    fun saveVote(vote: Vote, token: String): Long {
        println("Save: $vote")
        val userId = userService.getUserIdFromJwtToken(token)
        vote.fromUser = userId

        println("Folder: ${vote.onFolder}")
        if (vote.onFolder != null && !folderRepository.existsById(vote.onFolder ?: -1)) {
            throw NotFoundException("Missing folder!")
        }

        if (vote.onPack != null && !packRepository.existsById(vote.onPack ?: -1)) {
            throw NotFoundException("Missing pack!")
        }

        if (vote.onPack == null && vote.onFolder == null) {

            if (!userRepository.existsById(vote.toUser) || !userRepository.existsById(userId)) {
                throw NotFoundException("Missing user!")
            }

            val oldVote = getVoteByUser(vote.toUser, token)
            if (oldVote != null) {
                vote.id = oldVote.id
            }

            val id = votesRepository.save(vote).id
            updateVotesOnUser(vote.toUser)
            return id
        }

        if (vote.onPack != null) {
            val pack = packRepository.findById(vote.onPack ?: -1).get()
            val oldVote = getVoteByPack(vote.onPack, token)
            if (oldVote != null) {
                vote.id = oldVote.id
            }
            vote.toUser = pack.user_id

            if (!userRepository.existsById(vote.toUser) || !userRepository.existsById(userId)) {
                throw NotFoundException("Missing user!")
            }

            val id = votesRepository.save(vote).id
            updateVotesOnPack(vote.onPack ?: -1)
            return id
        }

        // The only way, the code can get here is when there is a folder.
        val folder = folderRepository.findById(vote.onFolder ?: -1).get()
        val oldVote = getVoteByFolder(vote.onFolder, token)
        if (oldVote != null) {
            vote.id = oldVote.id
        }
        vote.toUser = folder.user_id

        if (!userRepository.existsById(vote.toUser) || !userRepository.existsById(userId)) {
            throw NotFoundException("Missing user!")
        }

        val id = votesRepository.save(vote).id
        updateVotesOnFolder(vote.onFolder ?: -1)
        return id
    }

    fun removeVoteByUser(toUser: Long, token: String) {

        val userId = userService.getUserIdFromJwtToken(token)

        val votes = votesRepository.getVoteByUser(userId, toUser)

        votesRepository.delete(votes.first())
        updateVotesOnUser(toUser)
    }

    fun removeVoteByFolder(onFolder: Long?, token: String) {
        if (onFolder != null) {
            val userId = userService.getUserIdFromJwtToken(token)

            val votes = votesRepository.getVoteByFolder(userId, onFolder)

            votesRepository.delete(votes.first())
            updateVotesOnFolder(onFolder)
        }
    }

    fun removeVoteByPack(onPack: Long?, token: String) {
        if (onPack != null) {
            val userId = userService.getUserIdFromJwtToken(token)

            val votes = votesRepository.getVoteByPack(userId, onPack)

            votesRepository.delete(votes.first())
            updateVotesOnPack(onPack)
        }
    }

    fun updateVotesOnUser(userId: Long) {
        val user = userRepository.findById(userId).get()
        user.upVotes = votesRepository.getUpVotesOfUser(userId)
        user.downVotes = votesRepository.getDownVotesOfUser(userId)
        userRepository.save(user)
    }

    fun updateVotesOnPack(packId: Long) {
        val pack = packRepository.findById(packId).get()
        pack.upVotes = votesRepository.getUpVotesOfPack(packId)
        pack.downVotes = votesRepository.getDownVotesOfPack(packId)
        packRepository.save(pack)
        updateVotesOnUser(pack.user_id)
    }

    fun updateVotesOnFolder(folderId: Long) {
        val folder = folderRepository.findById(folderId).get()
        folder.upVotes = votesRepository.getUpVotesOfFolder(folderId)
        folder.downVotes = votesRepository.getDownVotesOfFolder(folderId)
        folderRepository.save(folder)
        updateVotesOnUser(folder.user_id)
    }

    fun deleteVote(vote: Vote, token: String) {
        val userId = userService.getUserIdFromJwtToken(token)
        var toDelete: List<Vote>

        if (vote.onPack != null) {
            toDelete = votesRepository.selectVote(
                userId,
                packRepository.findById(vote.onPack ?: -1).get().user_id,
                vote.onPack,
                vote.onFolder
            )

            if (toDelete.isNotEmpty()) {
                votesRepository.deleteById(toDelete.first().id)
                updateVotesOnPack(toDelete.first().onPack?: -1)
            }

            return
        }

        if (vote.onFolder != null) {
            toDelete = votesRepository.selectVote(
                userId,
                folderRepository.findById(vote.onFolder ?: -1).get().user_id,
                vote.onPack,
                vote.onFolder
            )

            if (toDelete.isNotEmpty()) {
                votesRepository.deleteById(toDelete.first().id)
                updateVotesOnFolder(toDelete.first().onFolder?: -1)
            }

            return
        }

        toDelete = votesRepository.selectVote(
            userId,
            vote.toUser,
            null,
            null
        )

        if (toDelete.isNotEmpty()) {
            votesRepository.deleteById(toDelete.first().id)
            updateVotesOnUser(toDelete.first().toUser)
        }

    }

}
