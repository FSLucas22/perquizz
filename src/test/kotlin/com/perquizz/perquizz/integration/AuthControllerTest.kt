package com.perquizz.perquizz.integration

import com.perquizz.perquizz.auth.dtos.AuthRequestDto
import com.perquizz.perquizz.builders.buildUser
import com.perquizz.perquizz.randomEmail
import com.perquizz.perquizz.randomString
import com.perquizz.perquizz.users.repositories.UserRepository
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : IntegrationTestWithSingletonDb() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should authenticate user with correct email and password`() {
        val password = randomString()
        val encodedPassword = passwordEncoder.encode(password)

        val user =
            buildUser {
                this.password = encodedPassword
            }

        userRepository.save(user)

        val request = AuthRequestDto(user.email, password)

        mockMvc.perform(
            post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isOk,
            jsonPath("$.username", equalTo(user.username)),
            jsonPath("$.token", notNullValue()),
        )
    }

    @Test
    fun `should return BadRequest when email does not belong to an user`() {
        val invalidEmail = randomEmail()
        val request = AuthRequestDto(invalidEmail, randomString())

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
        val user = buildUser()

        userRepository.save(user)

        val request = AuthRequestDto(user.email, "incorrectpass")

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
