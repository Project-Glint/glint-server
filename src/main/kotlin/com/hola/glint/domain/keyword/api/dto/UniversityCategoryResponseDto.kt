package com.hola.glint.domain.keyword.api.dto

import com.hola.glint.domain.keyword.entity.UniversityCategory
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "학교 카테고리 응답 데이터")
data class UniversityCategoryResponseDto(
    @Schema(description = "University Category ID", example = "1")
    var universityCategoryId: Long? = null,

    @Schema(description = "universityCategoryName", example = "명문대")
    var universityCategoryName: String? = null,
) {
    companion object {
        fun from(universityCategory: UniversityCategory?): UniversityCategoryResponseDto? {
            if (universityCategory == null) {
                return null
            }
            return UniversityCategoryResponseDto(
                universityCategoryId = universityCategory.id,
                universityCategoryName = universityCategory.universityCategoryName,
            )
        }
    }
}