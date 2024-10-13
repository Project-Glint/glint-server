package com.hola.glint.system.config

import com.hola.glint.domain.auth.application.PersistedRefreshTokenService
import com.hola.glint.security.*
import com.hola.glint.security.oauth2.CustomOAuth2Provider
import com.hola.glint.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.hola.glint.security.oauth2.OAuth2AuthenticationFailureHandler
import com.hola.glint.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.hola.glint.security.oauth2.user.CustomOAuth2UserService
import org.springframework.security.config.annotation.web.invoke
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(OAuth2ClientProperties::class)
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
    private val persistedRefreshTokenService: PersistedRefreshTokenService,
    private val appProperties: AppProperties,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val oAuth2ClientProperties: OAuth2ClientProperties,
) {
    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(tokenProvider, customUserDetailsService, persistedRefreshTokenService)
    }

    @Bean
    fun cookieAuthorizationRequestRepository(): AuthorizationRequestRepository<OAuth2AuthorizationRequest>? {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

    @Bean
    fun jwtExceptionFilter(): JwtExceptionFilter {
        return JwtExceptionFilter()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Order(1)
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { disable() }
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            formLogin { disable() }
            httpBasic { disable() }
            exceptionHandling {
                authenticationEntryPoint = RestAuthenticationEntryPoint()
                accessDeniedHandler = CustomAccessDeniedHandler()
            }
            authorizeHttpRequests {
                authorize("/v1/auth/**", permitAll)
                authorize("/oauth2/**", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/health", permitAll)

                authorize("/", permitAll)
                authorize("/error", permitAll)
                authorize("/robots.txt", permitAll)
                authorize("/favicon.ico", permitAll)
                authorize("/**/*.png", permitAll)
                authorize("/**/*.gif", permitAll)
                authorize("/**/*.svg", permitAll)
                authorize("/**/*.jpg", permitAll)
                authorize("/**/*.html", permitAll)
                authorize("/**/*.css", permitAll)
                authorize("/**/*.js", permitAll)
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(tokenAuthenticationFilter())
            addFilterBefore<TokenAuthenticationFilter>(jwtExceptionFilter())
            oauth2Login {
                authorizationEndpoint {
                    baseUri = "/oauth2/authorize"
                    authorizationRequestRepository = cookieAuthorizationRequestRepository()
                }
                redirectionEndpoint {
                    baseUri = "/oauth2/callback/*"
                }
                userInfoEndpoint {
                    userService = customOAuth2UserService
                }
                clientRegistrationRepository = clientRegistrationRepository(appProperties, oAuth2ClientProperties)
                authenticationSuccessHandler = oAuth2AuthenticationSuccessHandler
                authenticationFailureHandler = oAuth2AuthenticationFailureHandler
            }
        }
        return http.build()
    }

    @Bean
    fun clientRegistrationRepository(
        appProperties: AppProperties,
        oAuth2ClientProperties: OAuth2ClientProperties,
    ): ClientRegistrationRepository {
        val registrations = oAuth2ClientProperties.registration.entries
            .map {
                CustomOAuth2Provider
                    .getProvider(it.key)
                    .getBuilder(it.key, appProperties.oauth2.baseScheme)
                    .apply {
                        clientId(it.value.clientId)
                        clientSecret(it.value.clientSecret)
                    }
                    .build()
            }
            .filter { it.registrationId != "unknown" }
            .toMutableList()

        return InMemoryClientRegistrationRepository(registrations)
    }
}
