package com.perquizz.perquizz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PerquizzApplication

fun main(vararg args: String) {
    runApplication<PerquizzApplication>(*args)
}
