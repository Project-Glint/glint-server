package com.hola.glint.domain.keyword.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.keyword.api.dto.WorkResponseDto
import com.hola.glint.domain.keyword.entity.Work
import com.hola.glint.domain.keyword.repository.WorkRepository
import com.hola.glint.system.error.BadRequestException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WorkService(
    private val workRepository: WorkRepository,
    private val workCategoryService: WorkCategoryService,
//    private val workMappingService: WorkMappingService,
) {
    fun findById(workId: Long): WorkResponseDto { // work id를 통한 Work 엔티티 반환
        val work = workRepository.findById(workId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "Work not found with id: $workId") }
        return WorkResponseDto.from(work, work.workCategory)
    }

    fun findByName(workName: String): WorkResponseDto {
        val work: Work = getEntityByName(workName)
        return WorkResponseDto.from(work, work.workCategory)
    }

    fun getEntityByName(workName: String): Work {
        return workRepository.findByWorkName(workName)
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "Work not found with name: $workName")
    }

    fun createNewWork(workName: String): Work { // 이미 해당하는 workName을 가진 work가 있다면, 해당 객체를 반환하고, 없다면 work객체를 새로 생성하고 저장한 후 반환.
        val work = workRepository.findByWorkName(workName)
            ?: Work.createNewWork(workName)
        val workCategory = workCategoryService.findCategoryByWorkName(workName)
        work.updateWork(workName, workCategory)
        return workRepository.save(work)
    }

    @Transactional
    fun createNewWorkReturnDTO(workName: String): WorkResponseDto {
        val work = createNewWork(workName)
        return WorkResponseDto.from(work, work.workCategory)
    }

    @Transactional
    fun updateWorkById(workId: Long, workName: String): Work { // workId를 통한 직업명 업데이트
        val work: Work = workRepository.findById(workId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "Work not found with work id: $workId") }

        val workCategory = workCategoryService.findCategoryByWorkName(workName)

        work.updateWork(workName, workCategory)
        return workRepository.save(work)
    }

    @Transactional
    fun deleteWork(workId: Long) {
        val work: Work = workRepository.findById(workId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "Work not found with id: $workId") }
        workRepository.delete(work)
    }

    fun getAllWork(): List<WorkResponseDto> { // 직업 전체 조회
        return workRepository.findAll().map { WorkResponseDto.from(it, it.workCategory) }
    }

//    fun mapAllWorksToCategories() { // 초기 매핑 작업 (현재 DB에 있는 모든 work들의 카테고리를 매핑)
//        workMappingService.mapAllWorksToCategories()
//    }
}