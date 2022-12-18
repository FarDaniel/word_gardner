package aut.dipterv.word_gardner_backend.security

import com.fasterxml.jackson.annotation.JsonIgnore
import aut.dipterv.word_gardner_backend.model.WgUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class WgUserDetails(
    val id: Long,
    val userName: String,
    @JsonIgnore val pass: String,
    val authorities: MutableList<SimpleGrantedAuthority>
) : UserDetails {

    companion object {
        fun buildFromUser(wgUser: WgUser, authorities: MutableList<SimpleGrantedAuthority>): WgUserDetails {
            return WgUserDetails(wgUser.id ?: -1, wgUser.name, wgUser.password, authorities)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return pass
    }

    override fun getUsername(): String {
        return userName
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) {
            return true
        }
        if (other == null || this::class != other::class) return false
        val user = other as WgUserDetails
        return id == user.id
    }
}
