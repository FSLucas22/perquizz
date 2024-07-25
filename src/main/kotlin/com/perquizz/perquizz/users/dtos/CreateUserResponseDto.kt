package com.perquizz.perquizz.users.dtos

import java.time.LocalDateTime

data class CreateUserResponseDto(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
