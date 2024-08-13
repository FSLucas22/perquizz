package com.perquizz.perquizz.unit

import com.perquizz.perquizz.exceptions.BusinessException
import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.CreateUserResponseDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import com.perquizz.perquizz.users.services.UserService
import com.perquizz.perquizz.users.services.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.Optional

class UserServiceTest {
    private val repository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val service: UserService = UserServiceImpl(repository, passwordEncoder)

    @Test
    fun `should create a new user`() {
        val request =
            CreateUserRequestDto(
                "testuser",
                "testpass",
                "test@email.com",
            )

        val encryptedPassword = "akldnaiofan"

        val entity =
            UserEntity(
                "testuser",
                "test@email.com",
                encryptedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
            )

        every { passwordEncoder.encode("testpass") }.returns(encryptedPassword)
        every { repository.findByEmail("test@email.com") }.returns(null)
        every { repository.save(any()) }.returns(entity)

        val response: CreateUserResponseDto = service.createUser(request)

        val entitySlot = slot<UserEntity>()

        verify {
            passwordEncoder.encode("testpass")
            repository.save(capture(entitySlot))
        }

        assertThat(entitySlot.captured.password).isEqualTo(encryptedPassword)
        assertThat(response.id).isEqualTo(1L)
        assertThat(response.username).isEqualTo("testuser")
        assertThat(response.email).isEqualTo("test@email.com")
    }

    @Test
    fun `should not create user with repeated email`() {
        val request =
            CreateUserRequestDto(
                "testuser",
                "testpass",
                "test@email.com",
            )

        val encryptedPassword = "akldnaiofan"

        val entity =
            UserEntity(
                "testuser",
                "test@email.com",
                encryptedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
            )

        every { repository.findByEmail("test@email.com") }.returns(entity)

        val exception = catchThrowable { service.createUser(request) }

        assertThat(exception).isInstanceOf(BusinessException::class.java)

        val businessException = exception as BusinessException

        assertThat(businessException.type).isEqualTo("Invalid Email")
        assertThat(businessException.message).isEqualTo("Email already exists")
        assertThat(businessException.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should find user by id`() {
        val encryptedPassword = "akldnaiofan"

        val entity =
            UserEntity(
                "testuser",
                "test@email.com",
                encryptedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
            )

        every { repository.findById(1L) }.returns(Optional.of(entity))

        val response: CreateUserResponseDto = service.findUserById(1L)

        assertThat(response.id).isEqualTo(1L)
        assertThat(response.username).isEqualTo("testuser")
        assertThat(response.email).isEqualTo("test@email.com")
        assertThat(response.createdAt).isEqualTo(entity.updatedAt)
        assertThat(response.updatedAt).isEqualTo(entity.updatedAt)
    }

    @Test
    fun `should throw BusinessException when user id is not present in database`() {
        every { repository.findById(1L) }.returns(Optional.empty())

        val exception = catchThrowable { service.findUserById(1L) }

        val businessException = exception as BusinessException

        assertThat(businessException.type).isEqualTo("Invalid ID")
        assertThat(businessException.message).isEqualTo("ID does not belong to any user")
        assertThat(businessException.status).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
