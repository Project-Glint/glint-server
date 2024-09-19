package com.hola.glint.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        logger.error { "Responding with unauthorized error. Message - ${e.message}\t Request Path - ${request.requestURI}" }

        ExceptionResponse.responseUnauthorizedFail(response)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
