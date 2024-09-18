package com.hola.glint.system.config


import com.hola.glint.system.logtrace.LogTrace
import com.hola.glint.system.logtrace.ThreadLocalLogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfig {
    @Bean
    fun logTrace(): LogTrace {
        return ThreadLocalLogTrace()
    }
}
