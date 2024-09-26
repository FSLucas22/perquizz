package com.perquizz.perquizz.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.perquizz.perquizz.auth.services.TokenEmitterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class IntegrationTestSummary {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var tokenEmitterService: TokenEmitterService

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16.0-alpine3.18")
                .withStartupTimeout(Duration.ofSeconds(180))
    }
}
