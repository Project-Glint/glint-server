package com.hola.glint.domain.auth.entity

import com.hola.glint.domain.user.entity.User
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "refresh_token",
)
@EntityListeners(AuditingEntityListener::class)
@DynamicInsert
@DynamicUpdate
class PersistedRefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    var user: User,

    @Column(name = "refresh_token", nullable = false, unique = true, length = 255)
    var refreshToken: String,
)
