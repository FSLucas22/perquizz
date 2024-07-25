package com.perquizz.perquizz.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Suppress("SpreadOperator")
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, *publicGetEndpoints).permitAll()
                    .requestMatchers(HttpMethod.POST, *publicPostEndpoints).permitAll()
                    .anyRequest().authenticated()
            }.build()

    private val publicGetEndpoints = arrayOf("/api/v1/status", "/api/v1/migrations")
    private val publicPostEndpoints = arrayOf("/api/v1/migrations", "/api/v1/user")
}
