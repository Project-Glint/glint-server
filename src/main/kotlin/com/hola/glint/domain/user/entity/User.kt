package com.hola.glint.domain.user.entity

import com.hola.glint.domain.BaseTimeEntity
import com.hola.glint.domain.user.api.dto.UserRequestDto
import com.hola.glint.security.oauth2.AuthProvider
import jakarta.persistence.*

@Table(name = "user")
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val name: String? = null,

    @Column(
        name = "email",
        unique = true,
        nullable = false
    )
    val email: String,

    @Column(name = "role")
    val role: String?,

    @Column(name = "provider")
    val provider: AuthProvider,

    @Column(name = "kakao_provider_id", nullable = true, length = 127, updatable = true)
    var kakaoProviderId: String? = null,

    @Column(name = "archived")
    var archived: Boolean
) : BaseTimeEntity() {

    fun archive() {
        this.archived = true
    }

    companion object {
        fun createNewUser(userRequestDto: UserRequestDto): User {

            return User(
                name = userRequestDto.name,
                email = userRequestDto.email,
                archived = false,
                provider = userRequestDto.provider,
                kakaoProviderId = userRequestDto.getProviderId(AuthProvider.KAKAO),
                role = userRequestDto.role
            )
        }
    }
}