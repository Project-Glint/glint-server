package com.hola.glint.security

import com.hola.glint.common.utils.GlintUtil.getBearerToken
import com.hola.glint.domain.auth.application.PersistedRefreshTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
    private val persistedRefreshTokenService: PersistedRefreshTokenService,
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = getBearerToken(request)
            jwt?.let {
                val claims = tokenProvider.getClaims(jwt)
                val userId = claims.subject.toLong()
                checkNotNull(persistedRefreshTokenService.getPersistedRefreshToken(userId))
                val userDetails = customUserDetailsService.loadUserById(userId)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            log.error { "Could not set user authentication in security context: $ex" }
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        val log = KotlinLogging.logger {}
    }
}
