package com.hola.glint.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.system.error.ErrorResponse.Companion.of
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

object ExceptionResponse {
    fun responseUnauthorizedFail(response: HttpServletResponse) {
        val responseFail = of(ErrorCode.UNAUTHORIZED)

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(jacksonObjectMapper().writeValueAsString(responseFail))
    }

    fun responseBadExceptionFail(response: HttpServletResponse, errorCode: ErrorCode, message: String) {
        val responseFail = of(errorCode, message)

        response.status = HttpStatus.BAD_REQUEST.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(jacksonObjectMapper().writeValueAsString(responseFail))
    }

    fun responseInternalServerError(response: HttpServletResponse) {
        val responseError = of(ErrorCode.INTERNAL_SERVER_ERROR)

        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(jacksonObjectMapper().writeValueAsString(responseError))
    }
}
