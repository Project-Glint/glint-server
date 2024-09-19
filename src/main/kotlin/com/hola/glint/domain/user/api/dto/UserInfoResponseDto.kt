package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.User
import com.hola.glint.domain.user.entity.UserDetail
import com.hola.glint.domain.user.entity.UserProfile
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 전체 정보 응답 데이터")
data class UserInfoResponseDto(
    @Schema(description = "User ID", example = "1", nullable = false)
    var userId: Long?,
    @Schema(description = "User Detail 응답 데이터")
    var userDetailResponseDto: UserDetailResponseDto,
    @Schema(description = "User Profile 응답 데이터")
    var userProfileResponseDto: UserProfileResponseDto,
) {

    companion object {
        fun from(user: User, userDetail: UserDetail, userProfile: UserProfile) =
            UserInfoResponseDto(
                userId = user.id,
                userDetailResponseDto = UserDetailResponseDto.from(userDetail),
                userProfileResponseDto = UserProfileResponseDto.from(userProfile)
            )
    }
}
