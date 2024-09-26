package com.hola.glint.domain.user.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.keyword.application.LocationService
import com.hola.glint.domain.keyword.application.UniversityService
import com.hola.glint.domain.keyword.application.WorkService
import com.hola.glint.domain.user.api.dto.UserInfoResponseDto
import com.hola.glint.domain.user.api.dto.UserProfileRequest
import com.hola.glint.domain.user.api.dto.UserProfileResponseDto
import com.hola.glint.domain.user.entity.UserProfile
import com.hola.glint.domain.user.repository.UserProfileRepository
import com.hola.glint.system.error.BadRequestException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val universityService: UniversityService,
    private val workService: WorkService,
    private val locationService: LocationService,
    private val userService: UserService,
) {
    fun createEmptyUserProfile(userId: Long): UserProfileResponseDto {
        val userProfile = userProfileRepository.findByUserId(userId)

        if (userProfile == null) {
            val savedUserProfile = userProfileRepository.save(UserProfile.createEmptyProfile(userId))
            return UserProfileResponseDto.from(savedUserProfile)
        }

        return UserProfileResponseDto.from(userProfile)
    }

    fun getUserProfileById(userId: Long): UserProfileResponseDto {
        val userProfile: UserProfile = getUserProfileEntityById(userId)
        return UserProfileResponseDto.from(userProfile)
    }

    @Transactional
    fun updateUserProfile(userId: Long, userProfileRequest: UserProfileRequest): UserInfoResponseDto {
        val work = workService.createNewWork(userProfileRequest.workName)

        val university = universityService.getEntityByName(userProfileRequest.universityName, userProfileRequest.universityDepartment)
        val location = locationService.getEntityByName(userProfileRequest.locationState, userProfileRequest.locationCity)

        val userProfile: UserProfile = userProfileRepository.findByUserId(userId)
            ?: UserProfile.createNewUserProfile(
                userId,
                work,
                university,
                location,
                userProfileRequest.religion,
                userProfileRequest.smoking,
                userProfileRequest.drinking,
                userProfileRequest.selfIntroduction,
                userProfileRequest.hashtags
            )

        userProfile.updateUserProfile(
            work,
            university,
            location,
            userProfileRequest.religion,
            userProfileRequest.smoking,
            userProfileRequest.drinking,
            userProfileRequest.selfIntroduction,
            userProfileRequest.hashtags
        )
        userProfileRepository.save(userProfile)

        // TODO response 수정
        //  밑에 getUserInfo를 호출하지 않고 여기서 조합해야함.
        return userService.findUserInfoBy(userId)
    }

    private fun getUserProfileEntityById(userId: Long): UserProfile {
        return userProfileRepository.findByUserId(userId)
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "User Profile with userId: $userId not found")
    }
}