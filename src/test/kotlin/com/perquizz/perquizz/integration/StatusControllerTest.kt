package com.perquizz.perquizz.integration

import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class StatusControllerTest : IntegrationTestSummary() {
    @Test
    fun `should return status 200`() {
        val result = mockMvc.perform(get("/api/v1/status"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.updatedAt").isNotEmpty,
            jsonPath("$.dependencies.database.version", equalTo("16.0")),
            jsonPath("$.dependencies.database.maxConnections", equalTo(100)),
            jsonPath("$.dependencies.database.activeConnections").isNumber,
        )
    }
}
