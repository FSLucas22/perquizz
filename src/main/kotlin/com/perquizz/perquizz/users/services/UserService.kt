package com.perquizz.perquizz.users.services

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.CreateUserResponseDto

interface UserService {
    fun createUser(request: CreateUserRequestDto): CreateUserResponseDto

    fun findUserById(id: Long): CreateUserResponseDto
}
