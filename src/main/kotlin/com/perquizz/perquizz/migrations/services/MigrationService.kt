package com.perquizz.perquizz.migrations.services

import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto

interface MigrationService {
    fun getPendingMigrations(): MigrationsResponseDto

    fun migrate(): MigrationsResponseDto
}
