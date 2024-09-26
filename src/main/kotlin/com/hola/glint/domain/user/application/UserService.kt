package com.hola.glint.domain.user.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.user.api.dto.UserInfoResponseDto
import com.hola.glint.domain.user.api.dto.UserLoginResponseDto
import com.hola.glint.domain.user.api.dto.UserRequestDto
import com.hola.glint.domain.user.api.dto.UserResponseDto
import com.hola.glint.domain.user.entity.User
import com.hola.glint.domain.user.repository.UserDetailRepository
import com.hola.glint.domain.user.repository.UserProfileRepository
import com.hola.glint.domain.user.repository.UserRepository
import com.hola.glint.system.error.BadRequestException
import com.hola.glint.security.oauth2.AuthProvider
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userDetailRepository: UserDetailRepository,
    private val userProfileRepository: UserProfileRepository,
) {
    fun createUser(userRequestDto: UserRequestDto): User {
        val user = userRequestDto.toEntity()
        return userRepository.save(user)
    }

    fun getUserById(id: Long): UserResponseDto {
        val user = userRepository.findById(id)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "유저 정보를 찾을 수 없습니다.") }
        return UserResponseDto.from(user)
    }

    fun findUserInfoBy(userId: Long): UserInfoResponseDto {
        val user = userRepository.findById(userId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "유저 정보를 찾을 수 없습니다.") }

        val userDetail = userDetailRepository.findByUserId(userId)
        val userProfile = userProfileRepository.findByUserId(userId)
        return UserInfoResponseDto.from(user, userDetail!!, userProfile!!)
    }

    @Transactional
    fun oauthLoginUser(userRequest: UserRequestDto): UserLoginResponseDto {
        val user: User = userRequest.toEntity()
        val userOptional = userRepository.findByEmail(user.email)

        // 회원가입한 경우
        userOptional?.let {
            return UserLoginResponseDto.from(userRepository.save(userRequest.toEntity()), false)
        }

        val userDetailOptional = userDetailRepository.findByUserId(userOptional?.id)

        // 이미 회원가입은 했지만 detail 없는경우
        userDetailOptional?.let {
            return UserLoginResponseDto.from(userOptional, false)
        }
        // 이미 회원가입은 했지만 detail 있지만 완료하지 않은경우
        if (!userDetailOptional!!.isComplete) {
            return UserLoginResponseDto.from(userOptional, false)
        }

        // 이미 회원가입 했고 detail까지 완료한경우
        return UserLoginResponseDto.from(userOptional, true)
    }

    @Transactional
    fun getUserByProviderIdAndProvider(providerId: String, provider: AuthProvider): User? {
        return when (provider) {
            AuthProvider.KAKAO -> userRepository.findByKakaoProviderId(providerId)
            else -> null
        }
    }
}
