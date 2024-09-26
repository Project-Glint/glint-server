package com.hola.glint.domain.user.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.image.application.ImageService
import com.hola.glint.domain.user.api.dto.UserDetailRequestDto
import com.hola.glint.domain.user.api.dto.UserDetailResponseDto
import com.hola.glint.domain.user.api.dto.UserNickNameValidationResponseDto
import com.hola.glint.domain.user.entity.UserDetail
import com.hola.glint.domain.user.repository.UserDetailRepository
import com.hola.glint.system.error.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UserDetailService(
    private val userDetailRepository: UserDetailRepository,
    private val userProfileService: UserProfileService,
    private val imageService: ImageService,
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

    fun isNicknameTaken(nickname: String): UserNickNameValidationResponseDto {
        check(nickname.length in 3..15) {
            throw BadRequestException(ErrorCode.NICKNAME_INVALID)
        }

        val userDetailOptional = userDetailRepository.findByNickname(nickname)
        if (userDetailOptional != null) {
            throw BadRequestException(ErrorCode.NICKNAME_DUPLICATED)
        }

        return UserNickNameValidationResponseDto.from(true, nickname)
    }

    @Transactional
    fun createTempUserDetail(userId: Long, nickname: String): UserDetailResponseDto {
        check(nickname.length in 3..15) {
            throw BadRequestException(ErrorCode.NICKNAME_INVALID)
        }

        val userDetailOptional = userDetailRepository.findByNickname(nickname)

        if (userDetailOptional?.userId != userId) {
            throw BadRequestException(ErrorCode.NICKNAME_DUPLICATED)
        }

        val userDetail = userDetailRepository.findByUserId(userId)
            ?: userDetailRepository.save(UserDetail.createTempUserDetailByNickName(userId))

        userDetail.updateNickname(nickname)

        return UserDetailResponseDto.from(userDetail)
    }

    @jakarta.transaction.Transactional
    fun updateUserDetail(userId: Long, userDetailRequest: UserDetailRequestDto): UserDetailResponseDto {
        val userDetail: UserDetail = userDetailRepository.findByUserId(userId) ?: userDetailRequest.toEntity(userId)
        userProfileService.createEmptyUserProfile(userId)

        userDetail.updateUserDetail(
            userDetailRequest.nickname,
            userDetailRequest.gender,
            userDetailRequest.birthdate,
            userDetailRequest.height,
            userDetailRequest.profileImage
        )

        return UserDetailResponseDto.from(userDetailRepository.save(userDetail))
    }

    fun updateUserProfileImage(userId: Long, userProfileImageFile: MultipartFile): UserDetailResponseDto {
        val userDetail: UserDetail = userDetailRepository.findByUserId(userId)
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "UserDetail with userId: $userId not found")
        val imageResponse = imageService.uploadProfileImageFile(userProfileImageFile)
        userDetail.updateProfileUrl(imageResponse.url)
        return UserDetailResponseDto.from(userDetailRepository.save(userDetail))
    }
}
