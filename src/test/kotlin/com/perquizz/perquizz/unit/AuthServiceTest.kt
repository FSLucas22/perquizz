package com.perquizz.perquizz.unit

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.auth.dtos.AuthResponseDto
import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.AuthService
import com.perquizz.perquizz.auth.services.TokenEmitterService
import com.perquizz.perquizz.auth.services.impl.AuthServiceImpl
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.exceptions.BusinessException
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.LocalDateTime

class AuthServiceTest {
    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val tokenEmitterService: TokenEmitterService = mockk()
    private val authService: AuthService =
        AuthServiceImpl(
            userRepository,
            passwordEncoder,
            tokenEmitterService,
        )

    @Test
    fun `should authenticate user with correct email and password`() {
        val authRequest = AuthRequestDto("test@email.com", "testpass")

        val encryptedPassword = "aoindaodnadue"

        every {
            passwordEncoder.matches("testpass", encryptedPassword)
        } returns true

        every {
            userRepository.findByEmail("test@email.com")
        } returns
            UserEntity(
                "testuser",
                "test@email.com",
                encryptedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                id = 1L,
            )

        val token = Token("aodnadkan")
        val tokenRequest = TokenRequestDto(1L)
        val issueDate = Instant.now()

        every { tokenEmitterService.issueToken(tokenRequest, issueDate) } returns token

        val result = authService.authenticateUser(authRequest, issueDate)

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(AuthResponseDto("testuser", token))
    }

    @Test
    fun `should throw BusinessException when email does not belong to an user`() {
        val authRequest = AuthRequestDto("test@email.com", "testpass")

        every { userRepository.findByEmail("test@email.com") } returns null

        assertThatThrownBy { authService.authenticateUser(authRequest, Instant.now()) }
            .isInstanceOf(BusinessException::class.java)
            .usingRecursiveComparison()
            .isEqualTo(
                BusinessException(
                    "Authentication Failed",
                    "Invalid email or password",
                    HttpStatus.BAD_REQUEST,
                ),
            )
    }

    @Test
    fun `should throw BusinessException when password is incorrect`() {
        val authRequest = AuthRequestDto("test@email.com", "testpass")

        val encryptedPassword = "aoindaodnadue"

        every {
            passwordEncoder.matches("testpass", encryptedPassword)
        } returns false

        every {
            userRepository.findByEmail("test@email.com")
        } returns
            UserEntity(
                "testuser",
                "test@email.com",
                encryptedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
            )

        assertThatThrownBy { authService.authenticateUser(authRequest, Instant.now()) }
            .isInstanceOf(BusinessException::class.java)
            .usingRecursiveComparison()
            .isEqualTo(
                BusinessException(
                    "Authentication Failed",
                    "Invalid email or password",
                    HttpStatus.BAD_REQUEST,
                ),
            )
    }
}
