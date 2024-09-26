package com.hola.glint.domain.user.api.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 닉네임 Validation 응답 데이터")
data class UserNickNameValidationResponseDto(
    @Schema(
        description = "User Nickname",
        example = "철수",
        required = true
    )
    val nickname: String,
    @Schema(
        description = "User Nickname is available",
        example = "true",
        required = true
    )
    val isAvailable: Boolean
) {
    companion object {
        fun from(isAvailable: Boolean, nickname: String) =
            UserNickNameValidationResponseDto(
                nickname = nickname,
                isAvailable = isAvailable,
            )
    }
}
