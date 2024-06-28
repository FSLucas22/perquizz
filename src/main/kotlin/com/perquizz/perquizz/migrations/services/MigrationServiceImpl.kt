package com.perquizz.perquizz.migrations.services

import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.repository.MigrationRepository
import org.springframework.stereotype.Service

@Service
class MigrationServiceImpl(val repository: MigrationRepository) : MigrationService {
    override fun getPendingMigrations(): MigrationsResponseDto {
        return MigrationsResponseDto(repository.findPendingMigrations())
    }
}
