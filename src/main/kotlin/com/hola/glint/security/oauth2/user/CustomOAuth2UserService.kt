package com.hola.glint.security.oauth2.user

import com.hola.glint.domain.user.api.dto.UserRequestDto
import com.hola.glint.domain.user.application.UserService
import com.hola.glint.domain.user.entity.User
import com.hola.glint.security.UserPrincipal
import com.hola.glint.security.oauth2.user.OAuth2UserInfoFactory.getOAuth2UserInfo
import mu.KotlinLogging
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userService: UserService,
) : DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) { // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo: OAuth2UserInfo = getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        log.info { "[processOAuth2User] oauth login request: $oAuth2UserInfo" }

        if (oAuth2UserInfo.id.isEmpty()) {
            throw OAuth2AuthenticationException("Provider ID not found from OAuth2 provider")
        }

        val user = userService.getUserByProviderIdAndProvider(oAuth2UserInfo.id, oAuth2UserInfo.provider)
            ?: registerNewUser(oAuth2UserRequest, oAuth2UserInfo)

        log.info { "[processOAuth2User] oauth login user: ${user.id}" }
        return UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(
        oAuth2UserRequest: OAuth2UserRequest,
        oAuth2UserInfo: OAuth2UserInfo,
    ): User {
        return userService.createUser(
            UserRequestDto(
                provider = oAuth2UserInfo.provider,
                providerId = oAuth2UserInfo.id,
                email = oAuth2UserInfo.email,
                name = oAuth2UserInfo.name,
            )
        )
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
