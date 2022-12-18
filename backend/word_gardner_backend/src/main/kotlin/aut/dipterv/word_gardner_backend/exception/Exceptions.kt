package aut.dipterv.word_gardner_backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

sealed class ApiException(msg: String, val code: Int) : Exception(msg)

class NotFoundException(msg: String = "Something is missing.", code: Int = HttpStatus.NOT_FOUND.value()) :
    ApiException(msg, code)

class FormatViolationException(msg: String = "Wrong data format.", code: Int = HttpStatus.BAD_REQUEST.value()) :
    ApiException(msg, code)

class AuthorizationException(msg: String = "No authorization.", code: Int = HttpStatus.UNAUTHORIZED.value()) :
    ApiException(msg, code)

class TakenException(msg: String = "Something is already taken.", code: Int = HttpStatus.CONFLICT.value()) :
    ApiException(msg, code)

class InvalidTokenException(msg: String = "The Token was not valid.", code: Int = HttpStatus.UNAUTHORIZED.value()) :
    ApiException(msg, code)


@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(value = [ApiException::class])
    fun onApiException(ex: ApiException, response: HttpServletResponse): Unit =
        response.sendError(ex.code, ex.message)

    @ExceptionHandler(value = [NotImplementedError::class])
    fun onNotImplemented(ex: NotImplementedError, response: HttpServletResponse): Unit =
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value())

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun onConstraintViolation(ex: ConstraintViolationException, response: HttpServletResponse): Unit =
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.constraintViolations.joinToString(", ") { it.message })
}
