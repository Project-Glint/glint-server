package com.hola.glint.domain.keyword.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.keyword.api.dto.UniversityCategoryResponseDto
import com.hola.glint.domain.keyword.api.dto.UniversityResponseDto
import com.hola.glint.domain.keyword.entity.University
import com.hola.glint.domain.keyword.entity.UniversityCategory
import com.hola.glint.domain.keyword.repository.UniversityCategoryRepository
import com.hola.glint.domain.keyword.repository.UniversityRepository
import com.hola.glint.system.error.BadRequestException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UniversityService(
    private val universityRepository: UniversityRepository,
    private val universityCategoryRepository: UniversityCategoryRepository,
//    private val universityMappingService: UniversityMappingService,
) {

    fun findById(universityId: Long): UniversityResponseDto { // university id를 통한 University 응답 반환
        val university = universityRepository.findById(universityId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "University not found with id: $universityId") }
        return UniversityResponseDto.from(university, university.universityCategory)
    }

    fun findByName(
        universityName: String,
        universityDepartment: String
    ): UniversityResponseDto { // 대학교+학과명을 통한 University 응답 반환
        val university = universityRepository.findByUniversityNameAndUniversityDepartment(universityName, universityDepartment)
        return UniversityResponseDto.from(university, university.universityCategory)
    }

    fun getEntityByName(
        universityName: String,
        universityDepartment: String
    ): University { // 대학교+학과명을 통한 University 응답 반환
        return universityRepository.findByUniversityNameAndUniversityDepartment(universityName, universityDepartment)
    }

    fun getUniversitiesByName(universityName: String): List<UniversityResponseDto> { // 대학명을 통한 University 응답 리스트 반환
        return universityRepository.findByUniversityName(universityName).map {  UniversityResponseDto.from(it, it.universityCategory) }
    }

    fun allUniversity() = universityRepository.findAll().map { UniversityResponseDto.from(it, it.universityCategory) }

    fun allUniversityCategory() = universityCategoryRepository.findAll().map { UniversityCategoryResponseDto.from(it) }

    fun getUniversityCategoryByUniversity(university: University): UniversityCategory? {
        return university.universityCategory
    }

    /*@Transactional
    fun createNewUniversity(
        universityName: String,
        universityDepartment: String
    ): UniversityResponseDto? { // 우선 먼저 DB 에서 찾고 없으면 생성
        val universityCategory  = universityMappingService.determineUniversityCategory(universityName, universityDepartment)
        val university: University =
            universityRepository.findByUniversityNameAndUniversityDepartment(universityName, universityDepartment)
                .orElseGet {
                    universityRepository.save(
                        University.createNewUniversity(
                            universityName,
                            universityDepartment,
                            universityCategory
                        )
                    )
                }

        return UniversityResponseDto.from(university, universityCategory)
    }

    @Transactional
    fun updateUniversityById(
        universityId: Long,
        universityName: String?,
        universityDepartment: String?
    ): UniversityResponse {
        val university: University = universityRepository.findById(universityId)
            .orElseThrow { NotFoundEntityException("University not found with university id: $universityId") }
        val universityCategory: UniversityCategory =
            universityMappingService.determineUniversityCategory(universityName, universityDepartment)
        university.updateUniversity(universityName, universityDepartment, universityCategory)
        return UniversityResponse.from(universityRepository.save(university), universityCategory)
    }*/

    @Transactional
    fun deleteUniversity(universityId: Long) {
        val university: University = universityRepository.findById(universityId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "University not found with university id: $universityId") }
        universityRepository.delete(university)
    }
}
