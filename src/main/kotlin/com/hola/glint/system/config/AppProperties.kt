package com.hola.glint.system.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val auth: Auth,
    val oauth2: OAuth2,
) {

    data class Auth(
        val tokenSecret: String = "",
        val refreshTokenExpirationMsec: Long = 0,
        val tokenExpirationMsec: Long = 0,
    )

    data class OAuth2(
        val authorizedRedirectUris: List<String> = mutableListOf(),
        val baseScheme: String = "https",
    )
}
