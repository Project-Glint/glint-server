package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.keyword.entity.Location
import com.hola.glint.domain.keyword.entity.University
import com.hola.glint.domain.keyword.entity.Work
import com.hola.glint.domain.user.entity.UserProfile
import com.hola.glint.domain.user.entity.enumerated.DrinkingType
import com.hola.glint.domain.user.entity.enumerated.Religion
import com.hola.glint.domain.user.entity.enumerated.SmokingType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(title = "유저 프로필 요청 데이터")
data class UserProfileRequest(
    @Schema(
        description = "직장명",
        example = "삼성전자"
    )
    val workName: String,

    @Schema(
        description = "대학명",
        example = "중앙대학교"
    )
    val universityName: String,

    @Schema(
        description = "대학 학과명",
        example = "간호학과"
    )
    val universityDepartment: String,

    @Schema(
        description = "위치의 [시,도]",
        example = "서울"
    )
    val locationState: String,

    @Schema(
        description = "위치의 [시, 군, 구]",
        example = "강남구"
    )
    val locationCity: String,

    @Schema(
        description = "종교",
        example = "CATHOLIC"
    )
    val religion: Religion,

    @Schema(
        description = "흡연 타입",
        example = "NON_SMOKER"
    )
    val smoking: SmokingType,

    @Schema(
        description = "음주 타입",
        example = "NON_DRINKER"
    )
    val drinking: DrinkingType,

    @Schema(
        description = "자기소개",
        example = "안녕하세요! 저는 강아지를 좋아하고 활기찹니다."
    )
    @Size(max = 300, message = "자기소개는 최대 300자까지 가능합니다.")
    val selfIntroduction: String,

    @Schema(
        description = "나를 표현하는 키워드",
        example = "[\"적극적\", \"ESTJ\", \"애교많음\"]"
    )
    @Size(max = 10, message = "최대 10개의 키워드만 허용됩니다.")
    val hashtags: MutableList<String>?

) {
    fun toEntity(
        userId: Long,
        work: Work,
        university: University,
        location: Location,
        religion: Religion,
        smoking: SmokingType,
        drinking: DrinkingType
    ): UserProfile {
        return UserProfile.createNewUserProfile(
            userId,
            work,
            university,
            location,
            religion,
            smoking,
            drinking,
            selfIntroduction,
            hashtags
        )
    }
}