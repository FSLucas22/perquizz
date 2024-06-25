package com.perquizz.perquizz.status.dtos

import java.time.LocalDateTime

data class StatusResponseDto(
    val dependencies: DependenciesDto,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
