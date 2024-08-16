package com.perquizz.perquizz.users.services

import com.perquizz.perquizz.exceptions.BusinessException
import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.UserDetailsDto
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
    override fun createUser(request: CreateUserRequestDto): UserDetailsDto {
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

        return UserDetailsDto.from(user)
    }

    override fun findUserById(id: Long): UserDetailsDto =
        repository.findById(id).orElseThrow {
            BusinessException("Invalid ID", "ID does not belong to any user", HttpStatus.NOT_FOUND)
        }.let {
            UserDetailsDto.from(it)
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
