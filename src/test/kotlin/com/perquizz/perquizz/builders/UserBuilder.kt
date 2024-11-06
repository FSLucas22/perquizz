package com.perquizz.perquizz.builders

import com.perquizz.perquizz.randomEmail
import com.perquizz.perquizz.randomString
import com.perquizz.perquizz.users.entities.UserEntity
import java.time.LocalDateTime

fun buildUser(modifier: UserEntity.() -> Unit = {}): UserEntity =
    UserEntity(
        randomString(),
        randomEmail(),
        randomString(),
        LocalDateTime.now(),
        LocalDateTime.now(),
    ).apply(modifier)
