package com.perquizz.perquizz.unit

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.TokenEmitterService
import com.perquizz.perquizz.auth.services.impl.TokenEmitterServiceImpl
import com.perquizz.perquizz.configuration.JwtConfiguration
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class TokenEmitterServiceTest {
    private val expirationTimeSeconds: Long = 3600
    private val passwordKey: String = "testkey"
    private val jwtConfiguration = JwtConfiguration(passwordKey, expirationTimeSeconds)
    private val tokenEmitterService: TokenEmitterService =
        TokenEmitterServiceImpl(jwtConfiguration)

    @Test
    fun `should emit a token with correct data`() {
        val tokenRequest = TokenRequestDto(1L)
        val issueInstant = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val emittedToken = tokenEmitterService.issueToken(tokenRequest, issueInstant)

        val claims: Claims =
            Jwts.parser()
                .decryptWith(jwtConfiguration.password)
                .build()
                .parseEncryptedClaims(emittedToken.value)
                .payload

        assertThat(claims.subject).isEqualTo("1")
        assertThat(claims.issuedAt).isEqualTo(Date.from(issueInstant))
        assertThat(claims.expiration).isEqualTo(
            Date.from(issueInstant.plusSeconds(expirationTimeSeconds)),
        )
    }
}
