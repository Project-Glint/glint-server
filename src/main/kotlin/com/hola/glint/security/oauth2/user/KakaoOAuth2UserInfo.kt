package com.hola.glint.security.oauth2.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.hola.glint.security.oauth2.AuthProvider

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoOAuth2UserInfo(
    var id: Long = -1,
    var properties: Properties = Properties(),
    var kakaoAccount: KakaoAccount = KakaoAccount(),
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Properties(
        var nickname: String = "",
        var thumbnailImage: String = "",
        var profileImage: String = "",
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoAccount(
        var hasEmail: Boolean = false,
        var email: String = "",
        var isEmailValid: Boolean = false,
        var isEmailVerified: Boolean = false,
    )

    fun toOAuth2UserInfo(): OAuth2UserInfo {
        return OAuth2UserInfo(
            id = id.toString(),
            name = properties.nickname,
            imageUrl = properties.thumbnailImage,
            email = kakaoAccount.email,
            provider = AuthProvider.KAKAO,
        )
    }

    companion object {
        private val om = ObjectMapper()

        fun fromAttributeMap(attributes: Map<String, Any?>): KakaoOAuth2UserInfo {
            return om.convertValue(attributes, KakaoOAuth2UserInfo::class.java)
        }
    }
}
