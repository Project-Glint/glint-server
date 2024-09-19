package com.hola.glint.domain.user.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.user.api.dto.UserDetailRequestDto
import com.hola.glint.domain.user.api.dto.UserDetailResponseDto
import com.hola.glint.domain.user.entity.UserDetail
import com.hola.glint.domain.user.repository.UserDetailRepository
import com.hola.glint.system.error.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailService(
    private val userDetailRepository: UserDetailRepository,
//    private val userProfileService: UserProfileService,
//    private val imageService: ImageService? = null
) {
    @Transactional(readOnly = true)
    fun createUserDetail(userId: Long, userDetailRequest: UserDetailRequestDto): UserDetailResponseDto {
        val userDetail: UserDetail = userDetailRequest.toEntity(userId)
//        userProfileService.createEmptyUserProfile(userId)
        return UserDetailResponseDto.from(userDetailRepository.save(userDetail))
    }

    @Transactional(readOnly = true)
    fun getUserDetailById(userId: Long): UserDetailResponseDto {
        val userDetail = userDetailRepository.findByUserId(userId)
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "유저 정보를 찾을 수 없습니다.")
        return UserDetailResponseDto.from(userDetail)
    }

    @Transactional(readOnly = true)
    fun getUserDetailOptional(userId: Long): UserDetail? {
        return userDetailRepository.findByUserId(userId)
    }
}
