package com.hola.glint.domain.auth.api

import com.hola.glint.domain.auth.api.dto.Oauth2TokenRequestDto
import com.hola.glint.domain.auth.api.dto.Oauth2TokenResponseDto
import com.hola.glint.domain.auth.application.PersistedRefreshTokenService
import com.hola.glint.security.TokenProvider
import com.hola.glint.security.UserPrincipal
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

@RestController
class AuthController(
    private val tokenProvider: TokenProvider,
    private val persistedRefreshTokenService: PersistedRefreshTokenService,
    @Value("\${app.appScheme}") private val appScheme: String,
) {
    @PostMapping("/token")
    fun token(@RequestBody body: Oauth2TokenRequestDto): Oauth2TokenResponseDto {
        val persistedRefreshToken = persistedRefreshTokenService.getPersistedRefreshToken(body.refreshToken)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid refresh_token")
        val accessToken = tokenProvider.createAccessToken(persistedRefreshToken.user.id!!)
        val refreshToken = tokenProvider.createRefreshToken(persistedRefreshToken.user.id!!)
        persistedRefreshTokenService.persistRefreshToken(persistedRefreshToken.user.id!!, refreshToken)

        return Oauth2TokenResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresAt = LocalDateTime.ofInstant(
                tokenProvider.parseToken(accessToken).body.expiration.toInstant(),
                UTC,
            ),
            refreshTokenExpiresAt = LocalDateTime.ofInstant(
                tokenProvider.parseToken(refreshToken).body.expiration.toInstant(),
                UTC,
            ),
        )
    }

    @PostMapping("/revoke")
    fun logout(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Nothing> {
        persistedRefreshTokenService.revokeRefreshToken(userPrincipal.id)
        return ResponseEntity(
            HttpHeaders().apply { location = URI.create("$appScheme/signout") },
            HttpStatus.FOUND,
        )
    }
}
