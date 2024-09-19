package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode

/**
 * glint ASIS 에서 BusinessException 을 말함
 */
class BadRequestException(errorCode: ErrorCode, message: String? = null) :
    GlintException(errorCode = errorCode, message = errorCode.message)
