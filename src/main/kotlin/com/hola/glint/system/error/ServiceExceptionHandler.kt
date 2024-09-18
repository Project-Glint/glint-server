package com.hola.glint.system.error

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.system.error.ErrorResponse.Companion.of
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.nio.file.AccessDeniedException

@ControllerAdvice
class ServiceExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(ServiceExceptionHandler::class.java)


    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error("handleMethodArgumentNotValidException", e)
        val response: ErrorResponse = of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> {
        logger.error("handleBindException", e)
        val response: ErrorResponse = of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException?): ResponseEntity<ErrorResponse> {
        logger.error("handleMethodArgumentTypeMismatchException", e)
        val response: ErrorResponse = ErrorResponse.of(e)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException?): ResponseEntity<ErrorResponse> {
        logger.error("handleHttpRequestMethodNotSupportedException", e)
        val response: ErrorResponse = of(ErrorCode.METHOD_NOT_ALLOWED)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.METHOD_NOT_ALLOWED)
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(e: AccessDeniedException?): ResponseEntity<Any?> {
        logger.error("handleAccessDeniedException", e)
        val response: ErrorResponse = of(ErrorCode.HANDLE_ACCESS_DENIED)
        return ResponseEntity<Any?>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.status))
    }


//    /**
//     * DateTimeParsing 형식이 잘못된 경우 발생
//     */
//    @ExceptionHandler(DateTimeParseException::class)
//    protected fun handleDateTimeParseException(e: DateTimeParseException?): ResponseEntity<ErrorResponse> {
//        logger.error("handleDateTimeParseException", e)
//        val response: ErrorResponse = of(ErrorCode.DATETIME_PARSE_WRONG)
//        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
//    }
//
//    @ExceptionHandler(UnexpectedTypeException::class)
//    protected fun handleUnexpectedTypeException(e: UnexpectedTypeException?): ResponseEntity<ErrorResponse> {
//        logger.error("handleUnexpectedTypeException", e)
//        val response: ErrorResponse = of(ErrorCode.UNEXPECTED_TYPE)
//        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
//    }
//
//    @ExceptionHandler(BusinessException::class)
//    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
//        logger.error("handleEntityNotFoundException", e)
//        val errorCode: ErrorCode = e.getErrorCode()
//        val response: ErrorResponse = of(errorCode, e.getMessage())
//        return ResponseEntity<Any?>(response, HttpStatus.valueOf(errorCode.getStatus()))
//    }


    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception?): ResponseEntity<ErrorResponse> {
        logger.error("handleEntityNotFoundException", e)
        val response: ErrorResponse = of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
