package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode

open class GlintException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message
) : RuntimeException()
