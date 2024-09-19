package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.User
import com.hola.glint.security.oauth2.AuthProvider
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 생성 요청 데이터")
data class UserRequestDto(
    @Schema(
        description = "User Email",
        example = "glint@gmail.com",
        required = true
    )
    val email: String,

    @Schema(
        description = "Auth Provider",
        example = "KAKAO",
        required = true
    )
    val provider: AuthProvider,

    @Schema(
        description = "User Provider",
        example = "KAKAO",
        required = true
    )
    val providerId: String,

    @Schema(
        description = "User Role, (OAUTH_USER)",
        example = "OAUTH_USER",
        required = true
    )
    val role: String? = null,
) {
    fun toEntity(): User {
        return User.createNewUser(this)
    }

    fun getProviderId(findProvider: AuthProvider): String? {
        return if (findProvider == provider) providerId else null
    }

    companion object {
        fun of(email: String, role: String, provider: AuthProvider, providerId: String) =
            UserRequestDto(
                email = email,
                role = role,
                provider = provider,
                providerId = providerId,
            )

    }
}

