package com.perquizz.perquizz.auth.dtos

import java.time.Instant

data class TokenDto(
    val userId: Long,
    val issueInstant: Instant,
    val expirationInstant: Instant,
)
