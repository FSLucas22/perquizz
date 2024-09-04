package com.perquizz.perquizz.auth.services

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.valueobjects.Token
import java.time.Instant

interface TokenEmitterService {
    fun issueToken(
        request: TokenRequestDto,
        issueInstant: Instant,
    ): Token
}
