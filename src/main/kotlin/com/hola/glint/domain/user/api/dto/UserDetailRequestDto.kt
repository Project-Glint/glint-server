package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.user.entity.UserDetail
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class UserDetailRequestDto(
    @Schema(
        description = "닉네임",
        example = "철수",
        required = true
    ) val nickname: String,

    @Schema(
        description = "성별",
        example = "MALE|FEMALE",
        required = true
    )
    @Pattern(regexp = "(MALE|FEMALE)")
    val gender: String,

    @Schema(
        description = "생년월일",
        example = "2000-01-01",
        required = true
    )
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
    val birthdate: LocalDate,

    @Schema(
        description = "키",
        example = "180",
        required = true
    )
    val height: Int,

    @Schema(
        description = "프로필 이미지",
        example = "https://glint-image.s3.ap-northeast-2.amazonaws.com/profile/profile_1720106931.png",
        required = true
    )
    val profileImage: String
) {
    fun toEntity(userId: Long): UserDetail {
        return UserDetail.createNewUserDetail(
            userId,
            nickname,
            gender,
            birthdate,
            height,
            profileImage
        )
    }

    companion object {
        fun of(
            nickname: String,
            gender: String,
            birthdate: LocalDate,
            height: Int,
            profileImage: String
        ) = UserDetailRequestDto(
                nickname = nickname,
                gender = gender,
                birthdate = birthdate,
                height = height,
                profileImage = profileImage,
        )
    }
}
