package com.perquizz.perquizz.migrations.dtos

import liquibase.changelog.ChangeSet
import liquibase.database.Database

data class MigrationDto(
    val id: String,
    val author: String,
    val description: String,
    val changes: List<ChangeDto>,
) {
    companion object {
        fun from(
            changeSet: ChangeSet,
            database: Database,
        ) = MigrationDto(
            changeSet.id,
            changeSet.author,
            changeSet.description,
            changeSet.changes.map { ChangeDto.from(it, database) },
        )
    }
}
