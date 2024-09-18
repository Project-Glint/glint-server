package com.hola.glint.system.logtrace

import java.util.*

data class TraceId(
    val id: String = createId(),
    val level: Int = 0
) {

    companion object {
        private fun createId(): String {
            return UUID.randomUUID().toString().substring(0, 8)
        }
    }

    fun createNextId(): TraceId {
        return copy(level = level + 1)
    }

    fun createPreviousId(): TraceId {
        return copy(level = level - 1)
    }

    val isFirstLevel: Boolean
        get() = level == 0
}
