package com.hola.glint.system.logtrace

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class ThreadLocalLogTrace : LogTrace {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val traceIdHolder = ThreadLocal<TraceId?>()

    override fun begin(message: String?): TraceStatus {
        syncTraceId()
        val traceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()

        traceId?.let {
            logger.info("[{}] {}{}", it.id, addSpace(START_PREFIX, it.level), message)
        }

        return TraceStatus(traceId!!, startTimeMs, message)
    }

    override fun end(status: TraceStatus?) {
        complete(status, null)
    }

    override fun exception(status: TraceStatus?, e: Exception?) {
        complete(status, e)
    }


    private fun complete(status: TraceStatus?, e: Exception?) {
        val stopTimeMs = System.currentTimeMillis()
        val resultTimeMs = stopTimeMs - (status?.startTimeMs ?: 0)
        val traceId = status?.traceId

        if (traceId != null) {
            if (e == null) {
                logger.info(
                    "[{}] {}{} time={}ms",
                    traceId.id,
                    addSpace(COMPLETE_PREFIX, traceId.level),
                    status.message,
                    resultTimeMs
                )
            } else {
                logger.info(
                    "[{}] {}{} time={}ms ex={}",
                    traceId.id,
                    addSpace(EX_PREFIX, traceId.level),
                    status.message,
                    resultTimeMs,
                    e.toString()
                )
            }
        }

        releaseTraceId()
    }

    private fun syncTraceId() {
        val traceId = traceIdHolder.get()

        if (traceId == null) {
            traceIdHolder.set(TraceId())
        } else {
            traceIdHolder.set(traceId.createNextId())
        }
    }

    private fun releaseTraceId() {
        val traceId = traceIdHolder.get()

        if (traceId?.isFirstLevel == true) {
            traceIdHolder.remove()
        } else {
            traceIdHolder.set(traceId?.createPreviousId())
        }
    }

    companion object {
        private const val START_PREFIX = "-->"
        private const val COMPLETE_PREFIX = "<--"
        private const val EX_PREFIX = "<X-"

        private fun addSpace(prefix: String, level: Int): String {
            val sb = StringBuilder()

            for (i in 0 until level) {
                sb.append(if ((i == level - 1)) "|$prefix" else "|   ")
            }
            return sb.toString()
        }
    }
}
