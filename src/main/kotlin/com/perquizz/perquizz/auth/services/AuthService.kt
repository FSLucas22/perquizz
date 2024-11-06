package com.perquizz.perquizz.auth.services

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.auth.dtos.AuthResponseDto
import java.time.Instant

interface AuthService {
    fun authenticateUser(
        request: AuthRequestDto,
        issueDate: Instant,
    ): AuthResponseDto
}
