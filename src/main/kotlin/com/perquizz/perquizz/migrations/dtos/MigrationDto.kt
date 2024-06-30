package com.perquizz.perquizz.migrations.dtos

data class MigrationDto(
    val id: String,
    val author: String,
    val description: String,
    val changes: List<ChangeDto>,
)
