package com.perquizz.perquizz.integration

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.builders.buildUser
import com.perquizz.perquizz.randomEmail
import com.perquizz.perquizz.randomString
import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.repositories.UserRepository
import com.perquizz.perquizz.withAuthToken
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

class UserControllerTest : IntegrationTestWithSingletonDb() {
    @Autowired
    private lateinit var repository: UserRepository

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @Test
    fun `should create new user`() {
        val prevUser = buildUser()
        repository.save(prevUser)

        val newId = requireNotNull(prevUser.id) + 1

        val userEntity = buildUser()

        val newUser =
            CreateUserRequestDto(
                userEntity.username,
                userEntity.password,
                userEntity.email,
            )

        val performResult =
            mockMvc.perform(
                post("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(newUser)),
            )

        performResult.andExpectAll(
            status().isCreated(),
            header().string("location", "/api/v1/user/$newId"),
            jsonPath("$.username", equalTo(userEntity.username)),
            jsonPath("$.email", equalTo(userEntity.email)),
            jsonPath("$.createdAt", notNullValue()),
            jsonPath("$.updatedAt", notNullValue()),
            jsonPath("$.id", equalTo(newId.toInt())),
        )
    }

    @Test
    fun `should not create user with repeated email`() {
        val existingEmail = randomEmail()

        val existingUser = buildUser { email = existingEmail }

        repository.save(existingUser)

        val request =
            CreateUserRequestDto(
                randomString(),
                randomString(),
                existingEmail,
            )

        mockMvc.perform(
            post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isBadRequest,
            jsonPath("$.type", equalTo("Invalid Email")),
            jsonPath("$.message", equalTo("Email already exists")),
            jsonPath("$.status", equalTo(400)),
        )
    }

    @Test
    fun `should not return user information when user is not authenticated`() {
        mockMvc.perform(get("/api/v1/user/1"))
            .andExpectAll(status().isUnauthorized)
    }

    @Test
    fun `should find user by id`() {
        val existingUser = buildUser()

        repository.save(existingUser)

        val id = requireNotNull(existingUser.id)

        val token =
            tokenEmitterService.issueToken(
                TokenRequestDto(id),
                Instant.now(),
            )

        mockMvc.perform(
            get("/api/v1/user/$id")
                .withAuthToken(token),
        )
            .andExpectAll(
                status().isOk,
                jsonPath("$.username", equalTo(existingUser.username)),
                jsonPath("$.email", equalTo(existingUser.email)),
                jsonPath("$.createdAt", notNullValue()),
                jsonPath("$.updatedAt", notNullValue()),
                jsonPath("$.id", equalTo(id.toInt())),
            )
    }

    @Test
    fun `should return NOT FOUND when id does not belong to an user in database`() {
        val existingUser = buildUser()

        repository.save(existingUser)

        val id = requireNotNull(existingUser.id)

        val token = tokenEmitterService.issueToken(TokenRequestDto(id), Instant.now())

        mockMvc.perform(
            get("/api/v1/user/${id + 1}")
                .withAuthToken(token),
        )
            .andExpectAll(
                status().isNotFound,
                jsonPath("$.type", equalTo("Invalid ID")),
                jsonPath("$.message", equalTo("ID does not belong to any user")),
                jsonPath("$.status", equalTo(404)),
            )
    }
}
