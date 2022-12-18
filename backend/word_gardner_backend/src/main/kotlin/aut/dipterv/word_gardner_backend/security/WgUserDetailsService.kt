package aut.dipterv.word_gardner_backend.security

import aut.dipterv.word_gardner_backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service("UserDetailsService")
class WgUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        println("Username: $username")
        val userList = username?.let { userRepository.getUserByName(it) }
            ?: throw UsernameNotFoundException("No such user in the database")

        if (userList.isEmpty()) {
            throw UsernameNotFoundException("No such user in the database")
        }

        val user = userList.first()

        val authorities: MutableList<SimpleGrantedAuthority> = mutableListOf(SimpleGrantedAuthority("USER"))

        return WgUserDetails.buildFromUser(user, authorities)
    }
}