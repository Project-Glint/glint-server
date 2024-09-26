package com.hola.glint.domain.keyword.api.dto

import com.hola.glint.domain.keyword.entity.WorkCategory
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Work Category 응답 데이터")
class WorkCategoryResponseDto(
    @Schema(description = "Work Category ID", example = "1")
    var workCategoryId: Long? = null,

    @Schema(description = "Work Category Name", example = "1")
    var workCategoryName: String? = null,
) {
    companion object {
        fun from(workCategory: WorkCategory?): WorkCategoryResponseDto {
            return WorkCategoryResponseDto(
                workCategoryId = workCategory?.id,
                workCategoryName = workCategory?.workCategoryName,
            )
        }
    }
}
