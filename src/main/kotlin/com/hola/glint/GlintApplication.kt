package com.hola.glint

import com.hola.glint.system.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
@EnableJpaAuditing
class GlintApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<GlintApplication>(*args)
        }
    }
}
