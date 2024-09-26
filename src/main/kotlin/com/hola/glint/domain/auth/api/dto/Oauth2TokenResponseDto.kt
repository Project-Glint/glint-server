package com.hola.glint.domain.auth.api.dto

import java.time.LocalDateTime

data class Oauth2TokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: LocalDateTime,
    val refreshTokenExpiresAt: LocalDateTime,
)
