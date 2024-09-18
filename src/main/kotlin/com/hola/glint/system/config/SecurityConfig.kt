//package com.hola.glint.system.config
//
//import com.swyp.glint.auth.filter.JwtLoginFilter
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.Customizer
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
//// @Controller에 @Secured 메소드를 사용하여 간단히 권한 체크를 할 수 있다. @Secured('ROLE_MANAGER')
//// @PreAuthorize 어노테이션을 통해 권한점사 이전에 수행 여러 권한 허용할 때 @PreAuthorize("hasRole('ROLE_MANAGER')or haRole('ROLE_ADMIN')")
//// @postAuthorize
////@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
//class SecurityConfig {
//    private val jwtLoginFilter: JwtLoginFilter? = null
//
//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        val authenticationManager: AuthenticationManager = http.getSharedObject<AuthenticationManager>(
//            AuthenticationManager::class.java
//        )
//
//        return http
//            .csrf(Customizer<CsrfConfigurer<HttpSecurity?>> { obj: CsrfConfigurer<HttpSecurity?> -> obj.disable() }) //csrf 사용하지 않겠다.
//            .cors(Customizer<CorsConfigurer<HttpSecurity?>> { httpSecurityCorsConfigurer: CorsConfigurer<HttpSecurity?> ->
//                httpSecurityCorsConfigurer.configurationSource(
//                    corsConfigurationSource()
//                )
//            })
//            .httpBasic(Customizer<HttpBasicConfigurer<HttpSecurity?>> { obj: HttpBasicConfigurer<HttpSecurity?> -> obj.disable() }) //httpBasic 방식을 사용하지 않겠다.
//            .formLogin(Customizer<FormLoginConfigurer<HttpSecurity?>> { obj: FormLoginConfigurer<HttpSecurity?> -> obj.disable() }) //formLogin 방식을 사용하지 않겠다.
//            .sessionManagement(Customizer<SessionManagementConfigurer<HttpSecurity?>> { session: SessionManagementConfigurer<HttpSecurity?> ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            })
//            .authorizeHttpRequests(
//                Customizer<AuthorizationManagerRequestMatcherRegistry> { authorize: AuthorizationManagerRequestMatcherRegistry ->
//                    authorize //                                .requestMatchers("/users/**").hasRole("OAUTH_USER")
//                        //                                .requestMatchers("/meetings/**").hasRole("OAUTH_USER")
//                        //                                .requestMatchers( "/","/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        //                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                        .anyRequest().permitAll()
//                }
//            ) //UsernamePasswordAuthenticationFilter 필터 전에 jwtLoginFilter를 추가한다.
//            .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .build()
//    }
//
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        return CorsConfigurationSource { request: HttpServletRequest? ->
//            val config: CorsConfiguration = CorsConfiguration()
//            config.setAllowedHeaders(listOf("*"))
//            config.setAllowedMethods(listOf("*"))
//            config.setAllowedOriginPatterns(listOf("*")) //️ 허용할 origin
//            config.setAllowCredentials(true)
//            config.setAllowedOrigins(listOf("http://localhost:3000")) //️ 허용할 origin
//            config.setExposedHeaders(listOf("*"))
//            val source = UrlBasedCorsConfigurationSource()
//            config
//        }
//    }
//}
//
