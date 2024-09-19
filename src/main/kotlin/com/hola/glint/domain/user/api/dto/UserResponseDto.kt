package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 응답 데이터")
data class UserResponseDto(
    val id: Long?,
    @Schema(
        description = "User Email",
        example = "glint@gmail.com",
        required = true
    )
    val email: String,
    @Schema(
        description = "User Role, (OAUTH_USER)",
        example = "OAUTH_USER",
        required = true
    )
    val role: String?,
) {
    companion object {
        fun from(user: User): UserResponseDto =
            UserResponseDto(
                id = user.id,
                email = user.email,
                role = user.role,
            )
    }
}
