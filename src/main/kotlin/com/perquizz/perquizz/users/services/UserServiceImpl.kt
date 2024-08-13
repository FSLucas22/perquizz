package com.perquizz.perquizz.users.services

import com.perquizz.perquizz.exceptions.BusinessException
import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.CreateUserResponseDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {
    override fun createUser(request: CreateUserRequestDto): CreateUserResponseDto {
        validateUserEmail(request.email)

        val encodedPassword = passwordEncoder.encode(request.password)

        val user =
            repository.save(
                UserEntity(
                    request.username,
                    request.email,
                    encodedPassword,
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

    override fun findUserById(id: Long): CreateUserResponseDto =
        repository.findById(id).orElseThrow {
            BusinessException("Invalid ID", "ID does not belong to any user", HttpStatus.NOT_FOUND)
        }.let {
            CreateUserResponseDto(
                it.id!!,
                it.username,
                it.email,
                it.createdAt,
                it.updatedAt,
            )
        }

    private fun validateUserEmail(userEmail: String) =
        repository.findByEmail(userEmail)?.let {
            throw BusinessException(
                "Invalid Email",
                "Email already exists",
                HttpStatus.BAD_REQUEST,
            )
        }
}
