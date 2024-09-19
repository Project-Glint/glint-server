package com.hola.glint.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.system.error.ErrorResponse.Companion.of
import java.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AccessDeniedException
    ) {
        val responseFail = of(ErrorCode.HANDLE_ACCESS_DENIED)

        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(jacksonObjectMapper().writeValueAsString(responseFail))
    }
}
