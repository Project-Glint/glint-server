package com.hola.glint.domain.user.repository

import com.hola.glint.domain.user.entity.UserDetail
import com.hola.glint.domain.user.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDetailRepository : JpaRepository<UserDetail, Long> {
    fun findByNickname(nickname: String): UserDetail?

    fun findByUserId(userId: Long?): UserDetail?

    @Query("""select ud from UserDetail ud where ud.userId in :userIds""")
    fun findByUserIds(userIds: List<Long>): List<UserDetail>
}