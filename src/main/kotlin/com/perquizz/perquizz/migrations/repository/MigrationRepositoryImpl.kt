package com.perquizz.perquizz.migrations.repository

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import org.springframework.stereotype.Repository

@Repository
class MigrationRepositoryImpl() : MigrationRepository {
    override fun findPendingMigrations(): List<MigrationDto> {
        return listOf()
    }
}
