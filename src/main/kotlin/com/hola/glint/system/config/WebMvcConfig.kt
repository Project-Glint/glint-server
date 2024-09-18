package com.hola.glint.system.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods(*ALLOWED_METHOD_NAMES.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .exposedHeaders("*")
    }

    companion object {
        const val ALLOWED_METHOD_NAMES: String = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH"
    }
}
