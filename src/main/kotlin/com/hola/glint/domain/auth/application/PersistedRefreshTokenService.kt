package com.hola.glint.domain.auth.application

import com.hola.glint.domain.auth.entity.PersistedRefreshToken
import com.hola.glint.domain.auth.repository.RefreshTokenRepository
import com.hola.glint.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import jakarta.transaction.Transactional

@Service
class PersistedRefreshTokenService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun getPersistedRefreshToken(userId: Long): PersistedRefreshToken? {
        return refreshTokenRepository.getByUserId(userId)
    }

    fun getPersistedRefreshToken(refreshToken: String): PersistedRefreshToken? {
        return refreshTokenRepository.getByRefreshToken(refreshToken)
    }

    @Transactional
    fun persistRefreshToken(userId: Long, refreshToken: String) {
        val old = refreshTokenRepository.getByUserId(userId)
        if (old != null) {
            old.refreshToken = refreshToken
        } else {
            refreshTokenRepository.save(
                PersistedRefreshToken(
                    user = requireNotNull(userRepository.findById(userId).get()),
                    refreshToken = refreshToken
                )
            )
        }
    }

    @Transactional
    fun revokeRefreshToken(userId: Long) {
        refreshTokenRepository.deleteByUserId(userId)
    }
}
