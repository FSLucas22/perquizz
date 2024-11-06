package com.perquizz.perquizz.integration

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class IntegrationTestWithSingletonDb : IntegrationTestSummary() {
    companion object {
        @ServiceConnection
        @Container
        @JvmStatic
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16.0-alpine3.18")

        init {
            container.start()
        }
    }
}
