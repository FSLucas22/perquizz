package com.perquizz.perquizz.auth.controllers

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.auth.dtos.AuthResponseDto
import com.perquizz.perquizz.auth.services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val authService: AuthService) {
    @PostMapping
    fun authenticateUser(
        @RequestBody request: AuthRequestDto,
    ): ResponseEntity<AuthResponseDto> =
        ResponseEntity.ok().body(authService.authenticateUser(request, Instant.now()))
}
