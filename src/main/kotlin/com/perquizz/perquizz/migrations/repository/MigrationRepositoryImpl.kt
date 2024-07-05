package com.perquizz.perquizz.migrations.repository

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import org.flywaydb.core.Flyway
import org.springframework.stereotype.Repository

@Repository
class MigrationRepositoryImpl(private val flyway: Flyway) : MigrationRepository {
    override fun findPendingMigrations() =
        flyway.info().pending().map {
            MigrationDto(
                it.description,
                it.script,
                it.physicalLocation,
                it.version.name,
            )
        }

    override fun migrate() {
        flyway.migrate()
    }
}
