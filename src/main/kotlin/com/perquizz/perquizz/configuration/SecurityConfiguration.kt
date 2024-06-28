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
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, "/api/v1/status").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/migrations").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/migrations/delete").permitAll()
                    .anyRequest().authenticated()
            }.build()
}
