package com.perquizz.perquizz.configuration

import com.perquizz.perquizz.auth.services.impl.TokenFilterImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Suppress("SpreadOperator")
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        tokenFilter: TokenFilterImpl,
    ): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.GET, *publicGetEndpoints).permitAll()
                    .requestMatchers(HttpMethod.POST, *publicPostEndpoints).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    private val publicGetEndpoints = arrayOf("/api/v1/status", "/api/v1/migrations")
    private val publicPostEndpoints = arrayOf("/api/v1/migrations", "/api/v1/user", "api/v1/auth")
}
