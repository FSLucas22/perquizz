package com.perquizz.perquizz.unit

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.CreateUserResponseDto
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import com.perquizz.perquizz.users.services.UserService
import com.perquizz.perquizz.users.services.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserServiceTest {
    @Test
    fun `should create a new user`() {
        val repository: UserRepository = mockk()
        val service: UserService = UserServiceImpl(repository)

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

        every { repository.save(any()) }.returns(entity)

        val response: CreateUserResponseDto = service.createUser(request)

        verify {
            repository.save(any())
        }

        assertThat(response.id).isEqualTo(1L)
        assertThat(response.username).isEqualTo("testuser")
        assertThat(response.email).isEqualTo("test@email.com")
    }
}
