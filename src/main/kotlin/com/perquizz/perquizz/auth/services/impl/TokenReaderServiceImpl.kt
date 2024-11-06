package com.perquizz.perquizz.auth.services.impl

import com.perquizz.perquizz.auth.dtos.TokenDto
import com.perquizz.perquizz.auth.services.TokenReaderService
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.configuration.JwtConfiguration
import com.perquizz.perquizz.exceptions.InvalidTokenException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service

@Service
class TokenReaderServiceImpl(
    private val jwtConfiguration: JwtConfiguration,
) : TokenReaderService {
    override fun readToken(token: Token): TokenDto =
        try {
            Jwts.parser()
                .decryptWith(jwtConfiguration.password)
                .build()
                .parseEncryptedClaims(token.value)
                .payload.let {
                    TokenDto(
                        it.subject.toLong(),
                        it.issuedAt.toInstant(),
                        it.expiration.toInstant(),
                    )
                }
        } catch (e: JwtException) {
            val invalidTokenException = InvalidTokenException("Token inválido: ${token.value}")
            invalidTokenException.addSuppressed(e)
            throw invalidTokenException
        }
}
