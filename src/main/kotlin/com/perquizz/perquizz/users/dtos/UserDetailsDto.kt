package com.perquizz.perquizz.users.dtos

import com.perquizz.perquizz.users.entities.UserEntity
import java.time.LocalDateTime

data class UserDetailsDto(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(entity: UserEntity): UserDetailsDto =
            UserDetailsDto(
                entity.id!!,
                entity.username,
                entity.email,
                entity.createdAt,
                entity.updatedAt,
            )
    }
}
