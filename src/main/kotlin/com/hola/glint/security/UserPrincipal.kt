package com.hola.glint.security

import com.hola.glint.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    val id: Long,
    val email: String?,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>?,
) : UserDetails, OAuth2User {
    private var attributes: Map<String, Any>? = null

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getName(): String {
        return id.toString()
    }

    override fun getAttributes(): Map<String, Any> {
        return attributes ?: mapOf()
    }

    fun setAttributes(attributes: Map<String, Any>?) {
        this.attributes = attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return authorities
    }

    companion object {
        fun create(user: User): UserPrincipal {
            return UserPrincipal(
                user.id!!,
                user.email,
                "",
                listOf(),
            )
        }

        fun create(user: User, attributes: Map<String, Any>?): UserPrincipal {
            val userPrincipal = create(user)
            userPrincipal.setAttributes(attributes)
            return userPrincipal
        }
    }
}
