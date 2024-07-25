package com.perquizz.perquizz.users.services

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.CreateUserResponseDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val repository: UserRepository) : UserService {
    override fun createUser(request: CreateUserRequestDto): CreateUserResponseDto {
        val user =
            repository.save(
                UserEntity(
                    request.username,
                    request.email,
                    request.password,
                ),
            )

        return CreateUserResponseDto(
            user.id!!,
            user.username,
            user.email,
            user.createdAt,
            user.updatedAt,
        )
    }
}
