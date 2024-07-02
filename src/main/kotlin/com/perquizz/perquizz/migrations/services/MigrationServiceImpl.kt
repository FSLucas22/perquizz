package com.perquizz.perquizz.migrations.services

import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.repository.MigrationRepository
import org.springframework.stereotype.Service

@Service
class MigrationServiceImpl(val repository: MigrationRepository) : MigrationService {
    override fun getPendingMigrations(): MigrationsResponseDto {
        return MigrationsResponseDto(repository.findPendingMigrations())
    }

    override fun migrate(): MigrationsResponseDto {
        val pendingMigrations =
            MigrationsResponseDto(
                repository.findPendingMigrations(),
            )
        repository.migrate()
        return pendingMigrations
    }
}
