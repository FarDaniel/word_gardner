package aut.dipterv.word_gardner_backend.controller

import aut.dipterv.word_gardner_backend.dto.CountDto
import aut.dipterv.word_gardner_backend.dto.JwtToken
import aut.dipterv.word_gardner_backend.dto.LoginDto
import aut.dipterv.word_gardner_backend.dto.SearchFilter
import aut.dipterv.word_gardner_backend.model.Vote
import aut.dipterv.word_gardner_backend.model.WgUser
import aut.dipterv.word_gardner_backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@Validated
@RequestMapping("\${api.base-path:/api}")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/user"],
        consumes = ["application/json"]
    )
    fun createNewUser(
        @Valid @RequestBody body: LoginDto
    ): ResponseEntity<JwtToken> {
        userService.save(body)
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(body.name, body.password)
        )

        val jwt = userService.getJwtToken(authenticationManager, body)

        return ResponseEntity(jwt, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/user/login"],
        consumes = ["application/json"]
    )
    fun login(
        @Valid @RequestBody body: LoginDto
    ): ResponseEntity<JwtToken> {
        val jwt = userService.getJwtToken(authenticationManager, body)

        return ResponseEntity(jwt, HttpStatus.OK)
    }


    @RequestMapping(
        method = [RequestMethod.DELETE],
        value = ["/data/user/{Id}"]
    )
    fun deleteUser(
        @PathVariable("Id") id: java.math.BigDecimal
    ): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }


    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/user/{limit}/{offset}"],
        produces = ["application/json"]
    )
    fun getUsers(
        @PathVariable("limit") limit: Int,
        @PathVariable("offset") offset: Int
    ): ResponseEntity<List<WgUser>> {
        val response = ArrayList<WgUser>()
        response.addAll(userService.getUsers(limit, offset))
        return ResponseEntity(response, HttpStatus.OK)
    }


    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/data/user/{Id}"],
        produces = ["application/json"]
    )
    fun getUser(
        @PathVariable("Id") id: Long
    ): ResponseEntity<WgUser> {
        return ResponseEntity(userService.getUser(id), HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PUT],
        value = ["/data/user/{Id}"],
        consumes = ["application/json"]
    )
    fun updateUser(
        @PathVariable("Id") id: java.math.BigDecimal,
        @Valid @RequestBody body: WgUser
    ): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/user/filtered/{limit}/{offset}"],
        produces = ["application/json"]
    )
    fun getFilteredFolders(
        @PathVariable("limit") limit: Int,
        @PathVariable("offset") offset: Int,
        @Valid @RequestBody body: SearchFilter
    ): ResponseEntity<List<WgUser>> {
        val response = ArrayList<WgUser>()

        response.addAll(userService.getFilteredUsers(limit, offset, body))

        return ResponseEntity(response, HttpStatus.OK)
    }

    @RequestMapping(
        method = [RequestMethod.PATCH],
        value = ["/data/user/count"],
        produces = ["application/json"]
    )
    fun getUserCnt(
        @Valid @RequestBody body: SearchFilter,
    ): ResponseEntity<CountDto> {
        return ResponseEntity(userService.getUserCnt(body), HttpStatus.OK)
    }

}
