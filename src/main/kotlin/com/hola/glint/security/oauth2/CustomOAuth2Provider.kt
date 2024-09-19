package com.hola.glint.security.oauth2

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

// org.springframework.security.config.oauth2.client.CommonOAuth2Provider
enum class CustomOAuth2Provider {
    /*NAVER {
        override fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder =
            getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, getRedirectUrl(baseScheme))
                .apply {
                    scope("profile")
                    authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                    tokenUri("https://nid.naver.com/oauth2.0/token")
                    userInfoUri("https://openapi.naver.com/v1/nid/me")
                    userNameAttributeName("response")
                    jwkSetUri("temp")
                    clientName("Naver")
                    clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                }

        override fun getClientName(): String = "naver"
    },*/
    KAKAO {
        override fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder =
            getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, getRedirectUrl(baseScheme))
                .apply {
                    scope("profile_nickname", "profile_image")
                    authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    tokenUri("https://kauth.kakao.com/oauth/token")
                    userInfoUri("https://kapi.kakao.com/v2/user/me")
                    userNameAttributeName("id")
                    jwkSetUri("temp")
                    clientName("Kakao")
                    clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                }

        override fun getClientName(): String = "kakao"
    },
    /*GOOGLE {
        override fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder =
            getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, getRedirectUrl(baseScheme))
                .apply {
                    scope("profile", "email")
                    authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                    tokenUri("https://www.googleapis.com/oauth2/v4/token")
                    jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                    userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                    userNameAttributeName(IdTokenClaimNames.SUB)
                    clientName("Google")
                }

        override fun getClientName(): String = "google"
    },*/
    /*APPLE {
        override fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder =
            getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, getRedirectUrl(baseScheme))
                .apply {
                    scope("profile", "email")
                    authorizationUri("https://appleid.apple.com/auth/authorize")
                    tokenUri("https://appleid.apple.com/auth/token")
                    jwkSetUri("https://appleid.apple.com/auth/keys")
                    userNameAttributeName(IdTokenClaimNames.SUB)
                    clientName("Apple")
                }

        override fun getClientName(): String = "apple"
    },*/
    UNKNOWN {
        override fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder {
            TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
        }

        override fun getClientName(): String = "unknown"
    };

    companion object {
        fun getRedirectUrl(baseScheme: String) = "$baseScheme://{baseHost}{basePort}{basePath}/oauth2/callback/{registrationId}"
        fun getProvider(clientName: String): CustomOAuth2Provider {
            values().forEach {
                if (clientName == it.getClientName()) {
                    return it
                }
            }
            return UNKNOWN
        }
    }

    protected fun getBuilder(
        registrationId: String,
        method: ClientAuthenticationMethod?,
        redirectUri: String
    ): ClientRegistration.Builder {
        val builder = ClientRegistration.withRegistrationId(registrationId)
        builder.clientAuthenticationMethod(method)
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        builder.redirectUri(redirectUri)
        return builder
    }

    abstract fun getBuilder(registrationId: String, baseScheme: String): ClientRegistration.Builder

    abstract fun getClientName(): String
}
