package com.perquizz.perquizz.builders

import com.perquizz.perquizz.users.entities.UserEntity
import java.time.LocalDateTime

fun buildUser(modifier: UserEntity.() -> Unit = {}): UserEntity =
    UserEntity(
        "testuser",
        "test@email.com",
        "testpass",
        LocalDateTime.now(),
        LocalDateTime.now(),
    ).apply(modifier)
