package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 로그인 응답 데이터")
data class UserLoginResponseDto(
    @Schema(
        description = "User ID",
        example = "1",
        required = true
    )
    val id: Long?,
    
    @Schema(
        description = "User Email",
        example = "glint@gmail.com",
        required = true
    )
    val email: String?,
    
    @Schema(
        description = "기존회원, 회원가입 여부",
        example = "true",
        required = true
    ) 
    val isCompleteDetail: Boolean
) {
    companion object {
        fun from(user: User?, isCompleteDetail: Boolean?) =
        UserLoginResponseDto(
            id = user?.id,
            email = user?.email,
            isCompleteDetail = isCompleteDetail ?: false
        )
    }
}

