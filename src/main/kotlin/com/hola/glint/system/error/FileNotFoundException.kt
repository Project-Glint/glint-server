package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode

class FileNotFoundException(errorCode: ErrorCode, message: String? = null) :
    GlintException(errorCode = errorCode, message = errorCode.message)
