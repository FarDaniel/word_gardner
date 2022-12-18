package aut.dipterv.word_gardner_backend.service

import aut.dipterv.word_gardner_backend.dto.CountDto
import aut.dipterv.word_gardner_backend.dto.JwtToken
import aut.dipterv.word_gardner_backend.dto.LoginDto
import aut.dipterv.word_gardner_backend.dto.SearchFilter
import aut.dipterv.word_gardner_backend.exception.InvalidTokenException
import aut.dipterv.word_gardner_backend.exception.NotFoundException
import aut.dipterv.word_gardner_backend.exception.TakenException
import aut.dipterv.word_gardner_backend.model.WgUser
import aut.dipterv.word_gardner_backend.repository.UserRepository
import aut.dipterv.word_gardner_backend.security.jwt.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun getUserIdFromJwtToken(token: String): Long {
        return if (JwtUtils.validateJwtToken(token.replace("Bearer ", ""))) {
            val name = JwtUtils.getUserNameFromJwtToken(token.substring(7, token.length))
            val users = userRepository.getUserByName(name)
            if (users.isEmpty()) {
                throw NotFoundException("Missing user!")
            }
            users.first().id ?: -1
        } else {
            throw InvalidTokenException("Invalid JWT token!")
        }
    }

    fun getUser(id: Long): WgUser {
        if (!userRepository.existsById(id)) {
            throw NotFoundException("Missing user!")
        }

        return userRepository.findById(id).get().let {
            WgUser(
                id = it.id,
                upVotes = it.upVotes,
                downVotes = it.downVotes,
                name = it.name,
                password = "",
                picture = it.picture
            )
        }
    }

    fun getUsers(limit: Int, offset: Int): List<WgUser> {
        val usersWithPassword = userRepository.getUsers(limit, offset)
        val users = ArrayList<WgUser>()

        usersWithPassword.forEach {
            users.add(
                WgUser(
                    id = it.id,
                    upVotes = it.upVotes,
                    downVotes = it.downVotes,
                    name = it.name,
                    password = "",
                    picture = it.picture
                )
            )
        }

        return users
    }

    fun save(user: LoginDto): Long {
        if (userRepository.getUserByName(user.name).isNotEmpty()) {
            throw TakenException("This username is already taken.")
        }
        val savedUser = userRepository.save(
            WgUser(
                id = null,
                upVotes = 0,
                downVotes = 0,
                name = user.name,
                password = passwordEncoder.encode(user.password),
                picture = null
            )
        )
        return savedUser.id ?: -1
    }

    fun getUserCnt(filter: SearchFilter): CountDto {
        return CountDto(
            userRepository.getUserCnt(
                namePart = filter.namePart,
                minimalUpvotePercentage = filter.minimalUpvotePercentage,
                easyIncluded = filter.easyIncluded,
                mediumIncluded = filter.mediumIncluded,
                hardIncluded = filter.hardIncluded
            )
        )
    }

    fun getFilteredUsers(
        limit: Int,
        offset: Int,
        filter: SearchFilter
    ): List<WgUser> {
        val usersWithPassword = userRepository.getFiltered(
            namePart = filter.namePart,
            minimalUpvotePercentage = filter.minimalUpvotePercentage,
            easyIncluded = filter.easyIncluded,
            mediumIncluded = filter.mediumIncluded,
            hardIncluded = filter.hardIncluded,
            limit = limit,
            offset = offset
        )

        val users = ArrayList<WgUser>()

        usersWithPassword.forEach {
            users.add(
                WgUser(
                    id = it.id,
                    upVotes = it.upVotes,
                    downVotes = it.downVotes,
                    name = it.name,
                    password = "",
                    picture = it.picture
                )
            )
        }

        return users
    }

    fun getJwtToken(authenticationManager: AuthenticationManager, loginDto: LoginDto): JwtToken {

        if (userRepository.getUserByName(loginDto.name).isEmpty()) {
            throw NotFoundException("Missing user!")
        }

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginDto.name,loginDto.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        return JwtUtils.generateJwtToken(authentication)
    }

}
