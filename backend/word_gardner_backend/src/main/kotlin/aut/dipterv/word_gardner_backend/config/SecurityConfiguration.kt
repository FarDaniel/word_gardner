package aut.dipterv.word_gardner_backend.config

import aut.dipterv.word_gardner_backend.security.WgAuthenticationFilter
import aut.dipterv.word_gardner_backend.security.WgUserDetailsService
import aut.dipterv.word_gardner_backend.security.jwt.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration {

    @Value( "\${word_gardner_backend.app.jwtSecret}")
    lateinit var jwtSecret: String

    @Value("\${word_gardner_backend.app.jwtExpirationMs}")
    var jwtExpirationMs: Long = 0

    @Autowired
    lateinit var authenticationFilter: WgAuthenticationFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        JwtUtils.jwtSecret = jwtSecret
        JwtUtils.jwtExpirationMs = jwtExpirationMs
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            http.authorizeHttpRequests { auth ->
            auth.antMatchers("/api/data/**").hasAnyAuthority("USER")
            auth.antMatchers("/api/user").permitAll()
        }.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(withDefaults())
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration) = authConfig.authenticationManager

}
