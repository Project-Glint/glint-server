package com.hola.glint.domain.keyword.api.dto

import com.hola.glint.domain.keyword.entity.Work
import com.hola.glint.domain.keyword.entity.WorkCategory
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Work 응답 데이터")
class WorkResponseDto(
    @Schema(description = "Work ID", example = "1")
    var workId: Long? = null,

    @Schema(description = "직업명", example = "삼성전자")
    var workName: String? = null,

    @Schema(description = "Work Category 응답 데이터", example = "삼성전자")
    var workCategory: WorkCategoryResponseDto,
) {

    companion object {
        fun from(work: Work?, workCategory: WorkCategory?): WorkResponseDto {
            return WorkResponseDto(
                workId = work?.id,
                workName = work?.workName,
                workCategory = WorkCategoryResponseDto.from(workCategory),
            )
        }
    }
}
