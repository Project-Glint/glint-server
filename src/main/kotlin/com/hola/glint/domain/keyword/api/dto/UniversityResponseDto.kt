package com.hola.glint.domain.keyword.api.dto

import com.hola.glint.domain.keyword.entity.University
import com.hola.glint.domain.keyword.entity.UniversityCategory
import io.swagger.v3.oas.annotations.media.Schema


@Schema(title = "학교 응답 데이터")
data class UniversityResponseDto(
    @Schema(description = "University ID", example = "1")
    val universityId: Long? = null,

    @Schema(description = "대학명", example = "서울대학교")
    var universityName: String? = null,

    @Schema(description = "대학 학명", example = "의예과")
    var universityDepartment: String? = null,

    val universityCategory: UniversityCategoryResponseDto?,
) {
    companion object {
        fun from(university: University?, universityCategory: UniversityCategory?): UniversityResponseDto {
            return UniversityResponseDto(
                universityId = university?.id,
                universityName = university?.universityName,
                universityDepartment = university?.universityDepartment,
                universityCategory = UniversityCategoryResponseDto.from(universityCategory),
            )
        }
    }
}
