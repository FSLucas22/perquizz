package com.perquizz.perquizz.unit

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.repository.MigrationRepository
import com.perquizz.perquizz.migrations.services.MigrationServiceImpl
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MigrationServiceTest {
    val repository: MigrationRepository = mockk()
    val service = MigrationServiceImpl(repository)

    @Test
    fun `should return pending migrations`() {
        val pendingMigrations =
            listOf(
                MigrationDto(
                    "test",
                    "test",
                    "test",
                    "0.0.0",
                ),
            )

        every { repository.findPendingMigrations() } returns pendingMigrations

        val response = service.getPendingMigrations()

        assertThat(
            response,
        ).usingRecursiveAssertion().isEqualTo(MigrationsResponseDto(pendingMigrations))
    }
}
