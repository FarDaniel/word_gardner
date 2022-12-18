package aut.dipterv.word_gardner_backend.security.jwt

import aut.dipterv.word_gardner_backend.dto.JwtToken
import aut.dipterv.word_gardner_backend.security.WgUserDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.security.SignatureException
import java.util.*
import kotlin.jvm.internal.Intrinsics.Kotlin

object JwtUtils {
    lateinit var jwtSecret: String

    var jwtExpirationMs: Long = 0

    fun generateJwtToken(authentication: Authentication): JwtToken {
        val userPrincipal: WgUserDetails = authentication.principal as WgUserDetails
        val jwt = Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
            .compact()

        val userDetails: WgUserDetails = authentication.principal as WgUserDetails
        return JwtToken(jwt, userDetails.username)
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            println("Invalid JWT signature: ${e.message}")
        } catch (e: MalformedJwtException) {
            println("Invalid JWT token: ${e.message}")
        } catch (e: UnsupportedJwtException) {
            println("JWT token is unsupported: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println("JWT claims string is empty: ${e.message}")
        }
        return false
    }
}
