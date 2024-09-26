package com.hola.glint.domain.user.api.dto

import io.swagger.v3.oas.annotations.Parameter

data class UserNickNameRequestDto(
    @field:Parameter(
        description = "User Nickname",
        example = "철수",
        required = true
    ) @param:Parameter(
        description = "User Nickname",
        example = "철수",
        required = true
    ) val nickname: String
)
