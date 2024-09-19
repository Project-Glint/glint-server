package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.UserDetail
import io.swagger.v3.oas.annotations.media.Schema
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Function

@Schema(title = "유저 디테일 응답 데이터")
data class UserDetailResponseDto(
    @Schema(
        description = "UserDetail ID",
        example = "1",
        required = true
    )
    val id: Long?,
    @Schema(
        description = "User ID",
        example = "1",
        required = true
    )
    val userId: Long,
    @Schema(
        description = "User Nickname",
        example = "nickname",
        required = false
    )
    val nickname: String,
    @Schema(
        description = "User Gender",
        example = "gender",
        required = false
    )
    val gender: String,
    @Schema(
        description = "User Birthdate",
        example = "2000-01-01",
        required = false
    )
    val birthdate: String?,
    @Schema(
        description = "User Age",
        example = "20",
        required = false
    )
    val age: Int,

    @Schema(
        description = "User Height",
        example = "180",
        required = false
    )
    val height: Int,

    @Schema(
        description = "User Profile Image",
        example = "https://glint-image.s3.ap-northeast-2.amazonaws.com/profile/profile_1720106931.png",
        required = false
    )
    val profileImage: String?
) {
    companion object {
        fun from(userDetail: UserDetail) =
            UserDetailResponseDto(
                id = userDetail.id,
                userId = userDetail.userId,
                nickname = userDetail.nickname,
                gender = userDetail.gender,
                age = userDetail.calculateAgeByBirthdate(),
                birthdate = userDetail.birthdate?.format(DateTimeFormatter.ISO_DATE),
                height = userDetail.height,
                profileImage = userDetail.profileImage,
            )
    }
}