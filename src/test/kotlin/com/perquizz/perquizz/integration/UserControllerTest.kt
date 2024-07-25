package com.perquizz.perquizz.integration

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest : IntegrationTestSummary() {
    @Test
    fun `should create new user`() {
        val newUser = CreateUserRequestDto("testuser", "testpassword", "test@email.com")
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
}
