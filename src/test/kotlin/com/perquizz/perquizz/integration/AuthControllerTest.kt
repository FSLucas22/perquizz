package com.perquizz.perquizz.integration

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest : IntegrationTestSummary() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should authenticate user with correct email and password`() {
        val encodedPassword = passwordEncoder.encode("testpass")

        val user =
            UserEntity(
                "testuser",
                "test@email.com",
                encodedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
            )

        userRepository.save(user)

        val request = AuthRequestDto("test@email.com", "testpass")

        mockMvc.perform(
            post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isOk,
            jsonPath("$.username", equalTo("testuser")),
            jsonPath("$.token", notNullValue()),
        )
    }

    @Test
    fun `should return BadRequest when email does not belong to an user`() {
        val request = AuthRequestDto("invalid@email.com", "testpass")

        mockMvc.perform(
            post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isBadRequest,
            jsonPath("$.type", equalTo("Authentication Failed")),
            jsonPath("$.message", equalTo("Invalid email or password")),
            jsonPath("$.status", equalTo(400)),
        )
    }

    @Test
    fun `should return BadRequest when password is incorrect`() {
        val encodedPassword = passwordEncoder.encode("testpass")

        val user =
            UserEntity(
                "testuser",
                "test@email.com",
                encodedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
            )

        val request = AuthRequestDto("test@email.com", "incorrectpass")

        userRepository.save(user)

        mockMvc.perform(
            post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isBadRequest,
            jsonPath("$.type", equalTo("Authentication Failed")),
            jsonPath("$.message", equalTo("Invalid email or password")),
            jsonPath("$.status", equalTo(400)),
        )
    }
}
