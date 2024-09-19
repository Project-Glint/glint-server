package com.hola.glint.domain.user.repository

import com.hola.glint.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    @Query("""select u from User u where u.id in :ids""")
    fun findByIds(ids: List<Long>): List<User>

    fun findByKakaoProviderId(providerId: String): User?
}
