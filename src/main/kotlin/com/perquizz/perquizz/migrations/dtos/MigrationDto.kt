package com.perquizz.perquizz.migrations.dtos

data class MigrationDto(
    val description: String,
    val script: String,
    val location: String,
    val version: String,
)
