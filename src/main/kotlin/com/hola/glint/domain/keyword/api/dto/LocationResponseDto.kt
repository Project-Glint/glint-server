package com.hola.glint.domain.keyword.api.dto

import com.hola.glint.domain.keyword.entity.Location
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Location 응답 데이터")
data class LocationResponseDto(
    @Schema(description = "Location ID", example = "1")
    var locationId: Long? = null,

    @Schema(description = "위치의 [시,도]", example = "서울")
    val locationState: String,

    @Schema(description = "위치의 [시,군,구]", example = "강남구")
    var locationCity: String,
) {
    companion object {
        fun from(location: Location)
        = LocationResponseDto(
            locationId = location.id!!,
            locationState = location.state,
            locationCity = location.city,
        )
    }
}