package com.hola.glint.domain.keyword.application

import com.hola.glint.domain.keyword.api.dto.WorkCategoryResponseDto
import com.hola.glint.domain.keyword.entity.Work
import com.hola.glint.domain.keyword.entity.WorkCategory
import com.hola.glint.domain.keyword.repository.WorkCategoryRepository
import org.springframework.stereotype.Service

@Service
class WorkCategoryService(
    private val workCategoryRepository: WorkCategoryRepository
) {
    fun findCategoryByWorkName(workName: String): WorkCategory? {
        return workCategoryRepository.findByWorkNameContainingKeyword(workName)
    }

    fun getUWorkCategoryByWork(work: Work): WorkCategory? {
        return work.workCategory
    }

    fun getAllWorkCategory(): List<WorkCategoryResponseDto> { // 직업 카테고리 전체 조회
        return workCategoryRepository.findAll().map { WorkCategoryResponseDto.from(it) }
    }
}