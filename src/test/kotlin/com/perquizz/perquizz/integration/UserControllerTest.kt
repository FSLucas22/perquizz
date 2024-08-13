package com.perquizz.perquizz.integration

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest : IntegrationTestSummary() {
    @Autowired
    private lateinit var repository: UserRepository

    @Test
    fun `should create new user`() {
        val newUser =
            CreateUserRequestDto(
                "testuser",
                "testpassword",
                "test@email.com",
            )

        val performResult =
            mockMvc.perform(
                post("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(newUser)),
            )

        performResult.andExpectAll(
            status().isCreated(),
            header().string("location", "/api/v1/user/1"),
            jsonPath("$.username", equalTo("testuser")),
            jsonPath("$.email", equalTo("test@email.com")),
            jsonPath("$.createdAt", notNullValue()),
            jsonPath("$.updatedAt", notNullValue()),
            jsonPath("$.id", equalTo(1)),
        )
    }

    @Test
    fun `should not create user with repeated email`() {
        val existingUser =
            UserEntity(
                "testuser",
                "test@email.com",
                "asdlkafaj",
            )

        repository.save(existingUser)

        val request =
            CreateUserRequestDto(
                "newuser",
                "newpassword",
                "test@email.com",
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
    fun `should return correct resource url`() {
        val existingUser =
            UserEntity(
                "testuser",
                "test@email.com",
                "asdlkafaj",
            )

        repository.save(existingUser)

        val request =
            CreateUserRequestDto(
                "newuser",
                "newpassword",
                "newtest@email.com",
            )

        mockMvc.perform(
            post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)),
        ).andExpectAll(
            status().isCreated(),
            jsonPath("$.id", equalTo(2)),
            header().string("location", "/api/v1/user/2"),
        )
    }

    @Test
    fun `should find user by id`() {
        val existingUser =
            UserEntity(
                "testuser",
                "test@email.com",
                "asdlkafaj",
            )

        repository.save(existingUser)

        mockMvc.perform(get("/api/v1/user/1"))
            .andExpectAll(
                status().isOk,
                jsonPath("$.username", equalTo("testuser")),
                jsonPath("$.email", equalTo("test@email.com")),
                jsonPath("$.createdAt", notNullValue()),
                jsonPath("$.updatedAt", notNullValue()),
                jsonPath("$.id", equalTo(1)),
            )
    }

    @Test
    fun `should return NOT FOUND when id does not belong to an user in database`() {
        mockMvc.perform(get("/api/v1/user/1"))
            .andExpectAll(
                status().isNotFound,
                jsonPath("$.type", equalTo("Invalid ID")),
                jsonPath("$.message", equalTo("ID does not belong to any user")),
                jsonPath("$.status", equalTo(404)),
            )
    }
}
