package com.hola.glint.security

import com.hola.glint.common.utils.GlintUtil.getBearerToken
import com.hola.glint.domain.auth.application.PersistedRefreshTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val userDetailsService: UserDetailsService,
    private val persistedRefreshTokenService: PersistedRefreshTokenService,
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = getBearerToken(request)
        jwt?.let {
            val claims = tokenProvider.getClaims(jwt)
            val userId = claims.subject
            checkNotNull(persistedRefreshTokenService.getPersistedRefreshToken(userId.toLong()))
            val userDetails = userDetailsService.loadUserByUsername(userId)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}