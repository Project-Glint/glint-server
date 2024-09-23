package com.hola.glint.security.oauth2

import com.hola.glint.common.utils.CookieUtils.getCookie
import java.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    @Value("\${app.appScheme}") private val appScheme: String,
) :
    SimpleUrlAuthenticationFailureHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        var targetUrl = getCookie(
            request,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME,
        )?.value ?: "/"
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.localizedMessage)
            .build().toUriString()

        log.error { "OAuth2 authentication failure: ${exception.localizedMessage}" }
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, REDIRECT_APP_MAIN)
    }

    companion object {
        const val REDIRECT_APP_MAIN = "/api/v1/user/auth/redirect/main"
        val log = KotlinLogging.logger {}
    }
}
