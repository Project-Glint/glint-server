package com.hola.glint.domain.keyword.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.keyword.api.dto.WorkResponseDto
import com.hola.glint.domain.keyword.application.WorkCategoryService
import com.hola.glint.domain.keyword.application.WorkService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/work")
class WorkController(
    private val workService: WorkService,
    private val workCategoryService: WorkCategoryService,
) {
    @Operation(summary = "List all works", description = "모든 직업 조회")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllWorks() = ResponseData.success(workService.getAllWork())

    @GetMapping(path = ["/work"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a Work by workName", description = "직업명을 통한 직업 조회")
    fun getWorkByName(
        @RequestParam workName: String
    ): ResponseEntity<ResponseData<WorkResponseDto>> {
        return ResponseData.success(workService.findByName(workName))
    }

    @GetMapping(path = ["/{workId}/work"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a work by its ID", description = "work id를 통한 통한 직업 조회")
    fun getWorkById(@PathVariable workId: Long): ResponseEntity<ResponseData<WorkResponseDto>> {
        return ResponseData.success(workService.findById(workId))
    }

    @Operation(summary = "List all work categories", description = "모든 직업 카테고리 조회")
    @GetMapping(path = ["/categories"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allWorkCategories(): ResponseEntity<Any> = ResponseEntity.ok(workCategoryService.getAllWorkCategory())

    /*@PostMapping("/map-categories")
    @Operation(
        summary = "Map all existing works to categories",
        description = "초기 매핑 작업 : 모든 기존 Work 엔티티를 workCategoryId로 매핑하는 초기 작업을 수행"
    )
    fun mapAllWorksToCategories(): ResponseEntity<Void> {
        workService.mapAllWorksToCategories()
        return ResponseEntity.ok().build()
    }*/

    @PostMapping(path = ["/{workId}/work"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new work", description = "새로운 직업 생성")
    fun createWork(@RequestParam workName: String): ResponseEntity<ResponseData<WorkResponseDto>> {
        return ResponseData.created(workService.createNewWorkReturnDTO(workName))
    } 

    // 생성, 수정, 조회 주석 처리
}

