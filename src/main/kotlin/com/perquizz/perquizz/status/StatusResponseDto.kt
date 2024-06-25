package com.perquizz.perquizz.status

import java.time.LocalDateTime

data class DatabaseDto(val version: String, val maxConnections: Int, val activeConnections: Int)

data class DependenciesDto(val database: DatabaseDto)

data class StatusResponseDto(
    val dependencies: DependenciesDto,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
