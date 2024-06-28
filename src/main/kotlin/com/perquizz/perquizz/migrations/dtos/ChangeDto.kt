package com.perquizz.perquizz.migrations.dtos

import liquibase.change.Change
import liquibase.database.Database

data class ChangeDto(val description: String, val errors: List<String>, val warnings: List<String>) {
    companion object {
        fun from(
            change: Change,
            database: Database,
        ): ChangeDto {
            val validation = change.validate(database)
            return ChangeDto(
                change.description,
                validation.errorMessages,
                validation.warningMessages,
            )
        }
    }
}
