package com.hola.glint.security.oauth2.user

import com.hola.glint.security.oauth2.AuthProvider
import org.springframework.security.oauth2.core.OAuth2AuthenticationException

object OAuth2UserInfoFactory {
    @JvmStatic
    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when {
            /*registrationId.equals(AuthProvider.GOOGLE.toString(), ignoreCase = true) -> {
                GoogleOAuth2UserInfo.fromAttributeMap(attributes).toOAuth2UserInfo()
            }*/

            registrationId.equals(AuthProvider.KAKAO.toString(), ignoreCase = true) -> {
                KakaoOAuth2UserInfo.fromAttributeMap(attributes).toOAuth2UserInfo()
            }

            /*registrationId.equals(AuthProvider.NAVER.toString(), ignoreCase = true) -> {
                NaverOAuth2UserInfo.fromAttributeMap(attributes).toOAuth2UserInfo()
            }*/

            /*registrationId.equals(AuthProvider.APPLE.toString(), ignoreCase = true) -> {
                AppleOAuth2UserInfo.fromAttributeMap(attributes).toOAuth2UserInfo()
            }*/

            else -> {
                throw OAuth2AuthenticationException("Sorry! Login with $registrationId is not supported yet.")
            }
        }
    }
}
