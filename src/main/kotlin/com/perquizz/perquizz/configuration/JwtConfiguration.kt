package com.perquizz.perquizz.configuration

import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.Password
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt.config")
data class JwtConfiguration(
    private val passwordKey: String,
    val secondsToExpire: Long,
) {
    val password: Password = Keys.password(passwordKey.toCharArray())
}
