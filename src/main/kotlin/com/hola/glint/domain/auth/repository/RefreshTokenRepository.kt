package com.hola.glint.domain.auth.repository

import com.hola.glint.domain.auth.entity.PersistedRefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<PersistedRefreshToken, Long> {
    fun getByUserId(userId: Long): PersistedRefreshToken?
    fun deleteByUserId(userId: Long)
    fun getByRefreshToken(refreshToken: String): PersistedRefreshToken?
}
