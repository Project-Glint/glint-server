package com.hola.glint.system.logtrace

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect(private val logTrace: LogTrace) {

    @Around("com.hola.glint.system.logtrace.PointCuts.all()")
    @Throws(Throwable::class)
    fun logging(joinPoint: ProceedingJoinPoint): Any {
        var traceStatus: TraceStatus? = null

        try {
            val message: String = joinPoint.signature.toShortString()
            traceStatus = logTrace.begin(message)  // logTrace는 이제 non-null이므로 !! 사용 필요 없음

            val result: Any = joinPoint.proceed()

            logTrace.end(traceStatus)

            return result
        } catch (e: Exception) {
            traceStatus?.let {
                logTrace.exception(it, e)  // traceStatus가 null이 아닐 경우에만 exception 로깅
            }
            throw e
        }
    }
}
