package com.hola.glint.domain.keyword.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.keyword.api.dto.UniversityResponseDto
import com.hola.glint.domain.keyword.application.UniversityService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/university")
class UniversityController(
    private val universityService: UniversityService,
) {
    @GetMapping("/{universityId}")
    @Operation(summary = "Get a university by its ID", description = "University Id를 통한 대학 조회")
    fun getUniversityById(@PathVariable universityId: Long): ResponseEntity<ResponseData<UniversityResponseDto>> {
        return ResponseData.success(universityService.findById(universityId))
    }

    @GetMapping("/")
    @Operation(summary = "Get a university List by university name", description = "대학명을 통한 대학 리스트 조회")
    fun getUniversityByName(@RequestParam universityName: String): ResponseEntity<ResponseData<List<UniversityResponseDto>>> {
        return ResponseData.success(universityService.getUniversitiesByName(universityName))
    }

    @GetMapping("/list")
    @Operation(
        summary = "Get a university List by university name and university department name",
        description = "대학명과 학과명을 통한 대학 조회"
    )
    fun getUniversityByName(
        @RequestParam universityName: String,
        @RequestParam universityDepartment: String,
    ): ResponseEntity<ResponseData<UniversityResponseDto>> {
        return ResponseData.success(universityService.findByName(universityName, universityDepartment))
    }

    @GetMapping("/categories")
    @Operation(
        summary = "List all university categories",
        description = "모든 대학 카테고리 조회"
    )
    fun getAllUniversityCategories() = ResponseData.success(universityService.allUniversityCategory())

    // 생성, 수정, 조회 주석 처리
}