package com.hola.glint.common.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders

object GlintUtil {

    fun getBearerToken(request: HttpServletRequest): String? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)?.substring("Bearer ".length)
    }
}
