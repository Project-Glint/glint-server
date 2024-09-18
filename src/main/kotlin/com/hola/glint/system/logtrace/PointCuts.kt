package com.hola.glint.system.logtrace

import org.aspectj.lang.annotation.Pointcut

class PointCuts {
    @Pointcut("execution(* com.hola.glint.*(..))")
    fun all() {
    }
}
