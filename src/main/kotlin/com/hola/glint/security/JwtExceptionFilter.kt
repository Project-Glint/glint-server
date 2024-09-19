package com.hola.glint.security

import com.hola.glint.system.error.GlintException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter

class JwtExceptionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch(e: AuthenticationException) {
            ExceptionResponse.responseUnauthorizedFail(response)
        } catch(e: GlintException) {
            ExceptionResponse.responseInternalServerError(response)
        }
    }
}
