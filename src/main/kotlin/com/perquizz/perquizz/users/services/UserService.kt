package com.perquizz.perquizz.users.services

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.UserDetailsDto

interface UserService {
    fun createUser(request: CreateUserRequestDto): UserDetailsDto

    fun findUserById(id: Long): UserDetailsDto
}
