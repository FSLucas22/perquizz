package com.perquizz.perquizz.integration

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.randomLong
import com.perquizz.perquizz.withAuthToken
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

class TokenFilterIntegrationTest : IntegrationTestWithSingletonDb() {
    @Test
    fun `should return status Unauthorized when token is not from a user in database`() {
        val invalidToken =
            tokenEmitterService.issueToken(
                TokenRequestDto(randomLong()),
                Instant.now(),
            )

        mockMvc.perform(
            get("/api/v1/user/1")
                .withAuthToken(invalidToken),
        )
            .andExpectAll(status().isUnauthorized)
    }

    @Test
    fun `should return status Unauthorized when token is invalid`() {
        val invalidToken = Token("definitelyNotAToken")

        mockMvc.perform(
            get("/api/v1/user/1")
                .withAuthToken(invalidToken),
        )
            .andExpectAll(status().isUnauthorized)
    }
}
