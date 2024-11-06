package com.perquizz.perquizz.unit

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.repository.MigrationRepository
import com.perquizz.perquizz.migrations.services.MigrationServiceImpl
import io.mockk.Ordering
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MigrationServiceTest {
    private val repository: MigrationRepository = mockk()
    private val service = MigrationServiceImpl(repository)

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

    @Test
    fun `should execute and return pending migrations`() {
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
        every { repository.migrate() } just runs

        val response = service.migrate()

        assertThat(response)
            .usingRecursiveAssertion()
            .isEqualTo(MigrationsResponseDto(pendingMigrations))

        verify(Ordering.ORDERED) {
            repository.findPendingMigrations()
            repository.migrate()
        }
    }
}
