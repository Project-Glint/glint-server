package com.hola.glint.system.config

import com.hola.glint.common.utils.JwtUtil
import com.hola.glint.security.CustomAccessDeniedHandler
import com.hola.glint.security.JwtExceptionFilter
import com.hola.glint.security.RestAuthenticationEntryPoint
import com.hola.glint.security.TokenAuthenticationFilter
import com.hola.glint.security.oauth2.CustomOAuth2Provider
import com.hola.glint.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.hola.glint.security.oauth2.OAuth2AuthenticationFailureHandler
import com.hola.glint.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.hola.glint.security.oauth2.user.CustomOAuth2UserService
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(OAuth2ClientProperties::class)
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val appProperties: AppProperties,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val oAuth2ClientProperties: OAuth2ClientProperties,
) {
    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(jwtUtil)
    }

    @Bean
    fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

    @Bean
    fun jwtExceptionFilter(): JwtExceptionFilter {
        return JwtExceptionFilter()
    }

    @Bean
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
         http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(RestAuthenticationEntryPoint())
            .accessDeniedHandler(CustomAccessDeniedHandler())
            .and()
            .authorizeHttpRequests {
                it.requestMatchers("/v1/auth/**", "/oauth2/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/health").permitAll()
                    .anyRequest().authenticated()
            }.oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .clientRegistrationRepository(clientRegistrationRepository(appProperties, oAuth2ClientProperties))
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)

            return http
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(jwtExceptionFilter(), TokenAuthenticationFilter::class.java)
                .build()
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
