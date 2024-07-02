package com.perquizz.perquizz.migrations.repository

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import org.flywaydb.core.Flyway
import org.springframework.stereotype.Repository

@Repository
class MigrationRepositoryImpl(val flyway: Flyway) : MigrationRepository {
    override fun findPendingMigrations(): List<MigrationDto> {
        return flyway.info().pending().map {
            MigrationDto(
                it.description,
                it.script,
                it.physicalLocation,
                it.version.name,
            )
        }
    }

    override fun migrate() {
        TODO("Not yet implemented")
    }
}
