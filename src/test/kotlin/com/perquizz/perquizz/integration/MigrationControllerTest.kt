package com.perquizz.perquizz.integration

import org.flywaydb.core.Flyway
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.stringContainsInOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("db-test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MigrationControllerTest : IntegrationTestSummary() {
    @Autowired
    lateinit var flyway: Flyway

    @BeforeEach
    fun setUp() {
        flyway.clean()
    }

    private val api = "/api/v1"

    @Test
    fun `should return status 200 and migrations list`() {
        val result = mockMvc.perform(get("$api/migrations"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(1)),
            jsonPath("$.migrations[0].description", equalTo("test migration")),
            jsonPath("$.migrations[0].script", equalTo("V1__test_migration.sql")),
            jsonPath(
                "$.migrations[0].location",
                stringContainsInOrder(
                    "db-test",
                    "migration",
                    "V1__test_migration.sql",
                ),
            ),
        )
    }

    @Test
    fun `should return status 200 and execute migrations`() {
        val result = mockMvc.perform(post("$api/migrations"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(1)),
            jsonPath("$.migrations[0].description", equalTo("test migration")),
            jsonPath("$.migrations[0].script", equalTo("V1__test_migration.sql")),
            jsonPath(
                "$.migrations[0].location",
                stringContainsInOrder(
                    "db-test",
                    "migration",
                    "V1__test_migration.sql",
                ),
            ),
        )

        val getResult = mockMvc.perform(get("/api/v1/migrations"))
        getResult.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(0)),
        )

        val postResult = mockMvc.perform(post("/api/v1/migrations"))
        postResult.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(0)),
        )
    }
}
