package com.glint.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GlintApplication

fun main(args: Array<String>) {
    runApplication<GlintApplication>(*args)
}
