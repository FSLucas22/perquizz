package com.perquizz.perquizz.integration

import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

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
