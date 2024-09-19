package com.hola.glint.security.oauth2

import com.hola.glint.common.utils.CookieUtils.getCookie
import com.hola.glint.system.config.AppProperties
import com.hola.glint.domain.auth.application.PersistedRefreshTokenService
import com.hola.glint.security.TokenProvider
import com.hola.glint.security.UserPrincipal
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_GRANT
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: TokenProvider,
    private val persistedRefreshTokenService: PersistedRefreshTokenService,
    private val appProperties: AppProperties,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    @Value("\${app.appScheme}") private val appScheme: String,
) : SimpleUrlAuthenticationSuccessHandler() {
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)
        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val redirectUri = getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)?.value
        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw OAuth2AuthorizationException(
                OAuth2Error(INVALID_GRANT),
                "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
            )
        }
        val targetUrl = redirectUri ?: "$appScheme/oauth"
        val principal = authentication.principal as UserPrincipal
        val accessToken = tokenProvider.createAccessToken(principal.id)
        val refreshToken = tokenProvider.createRefreshToken(principal.id)
        persistedRefreshTokenService.persistRefreshToken(principal.id, refreshToken)
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("access_token", accessToken)
            .queryParam("refresh_token", refreshToken)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest?, response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        return appProperties.oauth2.authorizedRedirectUris
            .stream()
            .anyMatch { authorizedRedirectUri: String ->
                // Only validate host and port. Let the clients use different paths if they want to
                val authorizedURI = URI.create(authorizedRedirectUri)
                if (authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true) &&
                    authorizedURI.port == clientRedirectUri.port
                ) {
                    return@anyMatch true
                }
                false
            }
    }
}
