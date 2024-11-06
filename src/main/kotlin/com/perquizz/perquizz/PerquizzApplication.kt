package com.perquizz.perquizz

import com.perquizz.perquizz.configuration.JwtConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtConfiguration::class)
class PerquizzApplication

fun main(vararg args: String) {
    runApplication<PerquizzApplication>(*args)
}
