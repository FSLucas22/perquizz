package com.perquizz.perquizz.integration

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MigrationControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return status 200 and empty migrations list`() {
        val result = mockMvc.perform(get("/api/v1/migrations"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(0)),
        )
    }
}
