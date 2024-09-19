package com.hola.glint.security

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.user.repository.UserRepository
import com.hola.glint.system.error.BadRequestException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    fun loadUserById(userId: Long): UserDetails {
        val companyUser = userRepository.findById(userId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND) }
        return UserPrincipal.create(companyUser)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(fullname: String): UserDetails? {
        return null
    }
}
