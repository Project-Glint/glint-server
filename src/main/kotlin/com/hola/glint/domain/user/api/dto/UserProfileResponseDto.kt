package com.hola.glint.domain.user.api.dto

import com.hola.glint.domain.keyword.api.dto.UniversityResponseDto
import com.hola.glint.domain.keyword.api.dto.WorkResponseDto
import com.hola.glint.domain.keyword.entity.UniversityCategory
import com.hola.glint.domain.keyword.entity.WorkCategory
import com.hola.glint.domain.user.entity.UserProfile
import com.hola.glint.domain.user.entity.enumerated.DrinkingType
import com.hola.glint.domain.user.entity.enumerated.Religion
import com.hola.glint.domain.user.entity.enumerated.SmokingType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "유저 프로필 응답 데이터")
data class UserProfileResponseDto(
    @Schema(description = "User Profile ID", example = "1", nullable = false)
    private val id: Long? = null,

    private val work: WorkResponseDto? = null,
    private val university: UniversityResponseDto? = null,

//    private val location: LocationResponse? = null

    @Schema(description = "User ID", example = "1", nullable = false)
    private val userId: Long? = null,

    @Schema(description = "담배", example = "비흡연")
    private val smokingType: SmokingType? = null,

    @Schema(description = "종교", example = "기독교")
    private val religion: Religion? = null,

    @Schema(description = "술", example = "마시지 않음")
    private val drinkingType: DrinkingType? = null,

    @Schema(description = "자기소개", example = "안녕하세요 저는 서울 강북구에 사는 유재석이라고 합니다.")
    private val selfIntroduction: String? = null,

    @Schema(description = "나를 표현하는 키워드", example = "[적극적, ESTJ, 애교많음]")
    private val hashtags: List<String>? = null,
) {
    companion object {
        fun from(userProfile: UserProfile) =
            UserProfileResponseDto (
                userId = userProfile.userId,
                work = WorkResponseDto.from(userProfile.work, userProfile.work?.workCategory),
                university = UniversityResponseDto.from(userProfile.university, userProfile.university?.universityCategory),
                smokingType = userProfile.smokingType,
                religion = userProfile.religion,
                drinkingType = userProfile.drinkingType,
                selfIntroduction = userProfile.selfIntroduction,
                hashtags = userProfile.hashtags
            )
    }
}