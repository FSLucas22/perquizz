package com.perquizz.perquizz.auth.services.impl

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.auth.dtos.AuthResponseDto
import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.AuthService
import com.perquizz.perquizz.auth.services.TokenEmitterService
import com.perquizz.perquizz.exceptions.BusinessException
import com.perquizz.perquizz.users.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenEmitterService: TokenEmitterService,
) : AuthService {
    override fun authenticateUser(
        request: AuthRequestDto,
        issueDate: Instant,
    ): AuthResponseDto =
        userRepository.findByEmail(request.email) ?.takeIf {
            passwordEncoder.matches(request.password, it.password)
        }?.let {
            val id = requireNotNull(it.id)
            val tokenRequest = TokenRequestDto(id)
            val token = tokenEmitterService.issueToken(tokenRequest, issueDate)
            AuthResponseDto(it.username, token)
        } ?: throw BusinessException(
            "Authentication Failed",
            "Invalid email or password",
            HttpStatus.BAD_REQUEST,
        )
}
