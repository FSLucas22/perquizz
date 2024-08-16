package com.perquizz.perquizz.users.dtos

data class CreateUserRequestDto(
    val username: String,
    val password: String,
    val email: String,
)
