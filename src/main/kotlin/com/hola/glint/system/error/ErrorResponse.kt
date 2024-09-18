package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

data class ErrorResponse(
    val message: String,
    val status: Int,
    val errors: List<FieldError>?,
    val code: String
) {
    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(
                message = code.message,
                status = code.status,
                errors = FieldError.of(bindingResult),
                code = code.code
            )
        }

        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(
                message = code.message,
                status = code.status,
                errors = emptyList(),
                code = code.code
            )
        }

        fun of(code: ErrorCode, message: String): ErrorResponse {
            return ErrorResponse(
                message = message,
                status = code.status,
                errors = emptyList(),
                code = code.code
            )
        }

        fun of(code: ErrorCode, errors: List<FieldError>): ErrorResponse {
            return ErrorResponse(
                message = code.message,
                status = code.status,
                errors = errors,
                code = code.code
            )
        }

        fun of(e: MethodArgumentTypeMismatchException?): ErrorResponse {
            val value = e?.value?.toString() ?: ""
            val errors = e?.name?.let { FieldError.of(it, value, e.errorCode) }
            return ErrorResponse(
                message = ErrorCode.INVALID_TYPE_VALUE.message,
                status = ErrorCode.INVALID_TYPE_VALUE.status,
                errors = errors,
                code = ErrorCode.INVALID_TYPE_VALUE.code
            )
        }
    }

    data class FieldError(
        val field: String,
        val value: String,
        val reason: String?
    ) {
        companion object {
            fun of(field: String, value: String, reason: String?): List<FieldError> {
                return listOf(FieldError(field, value, reason))
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                return bindingResult.fieldErrors.map { error ->
                    FieldError(
                        field = error.field,
                        value = error.rejectedValue?.toString() ?: "",
                        reason = error.defaultMessage
                    )
                }
            }
        }
    }
}

