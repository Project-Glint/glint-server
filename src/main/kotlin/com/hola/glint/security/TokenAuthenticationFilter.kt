package com.hola.glint.security

import com.hola.glint.common.utils.GlintUtil.getBearerToken
import com.hola.glint.common.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

class TokenAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = getBearerToken(request)
        jwt?.let {
            val claims = jwtUtil.getClaims(jwt)

            val userId = claims.subject
            val authorities = claims["roles"] as List<String>
            val userDetails = UserPrincipal(
                id = userId.toLong(),
                email = null,
                password = null,
                authorities = authorities.map { SimpleGrantedAuthority(it) }
            )
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities.map { SimpleGrantedAuthority(it) })
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}