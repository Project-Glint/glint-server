package com.hola.glint.security.oauth2

import com.hola.glint.common.utils.CookieUtils.addCookie
import com.hola.glint.common.utils.CookieUtils.deleteCookie
import com.hola.glint.common.utils.CookieUtils.deserialize
import com.hola.glint.common.utils.CookieUtils.getCookie
import com.hola.glint.common.utils.CookieUtils.serialize
import com.nimbusds.oauth2.sdk.util.StringUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            ?.let { deserialize(it, OAuth2AuthorizationRequest::class.java) }
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest, response: HttpServletResponse) {
        if (authorizationRequest == null) {
            deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
            deleteCookie(request, response, REQUEST_ADDITIONAL_OAUTH2)
            return
        }
        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), cookieExpireSeconds)
        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
        val requestAdditionalOauth2 = request.getParameter(REQUEST_ADDITIONAL_OAUTH2)
        if (StringUtils.isNotBlank(requestAdditionalOauth2)) {
            addCookie(response, REQUEST_ADDITIONAL_OAUTH2, requestAdditionalOauth2, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): OAuth2AuthorizationRequest? {
        return this.loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        const val REQUEST_ADDITIONAL_OAUTH2 = "request_additional_oauth2"

        private const val cookieExpireSeconds = 180
    }
}
