package com.perquizz.perquizz.migrations.repository

import com.perquizz.perquizz.migrations.dtos.MigrationDto

interface MigrationRepository {
    fun findPendingMigrations(): List<MigrationDto>

    fun migrate()
}
