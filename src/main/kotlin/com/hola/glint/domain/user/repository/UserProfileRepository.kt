package com.hola.glint.domain.user.repository

import com.hola.glint.domain.user.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserProfileRepository : JpaRepository<UserProfile, Long> {

    fun findByUserId(userId: Long): UserProfile?

    @Query("""select up from UserProfile up where up.userId in :userIds""")
    fun findByUserIds(userIds: List<Long>): List<UserProfile>
}
