package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode

class UnauthorizedException(responseCode: ErrorCode, message: String? = null) :
    GlintException(errorCode = responseCode, message = message)
