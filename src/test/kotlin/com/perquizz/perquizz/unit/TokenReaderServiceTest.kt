package com.perquizz.perquizz.unit

import com.perquizz.perquizz.auth.dtos.TokenDto
import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.TokenReaderService
import com.perquizz.perquizz.auth.services.impl.TokenEmitterServiceImpl
import com.perquizz.perquizz.auth.services.impl.TokenReaderServiceImpl
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.configuration.JwtConfiguration
import com.perquizz.perquizz.exceptions.InvalidTokenException
import io.jsonwebtoken.JwtException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TokenReaderServiceTest {
    private val expirationTimeSeconds: Long = 3600
    private val passwordKey: String = "testkey"
    private val jwtConfiguration = JwtConfiguration(passwordKey, expirationTimeSeconds)
    private val tokenEmitterService = TokenEmitterServiceImpl(jwtConfiguration)
    private val tokenReaderService: TokenReaderService =
        TokenReaderServiceImpl(jwtConfiguration)

    @Test
    fun `should correctly return token data`() {
        val now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val token = tokenEmitterService.issueToken(TokenRequestDto(1L), now)
        val tokenDto: TokenDto = tokenReaderService.readToken(token)

        assertThat(tokenDto)
            .usingRecursiveComparison()
            .isEqualTo(
                TokenDto(
                    1L,
                    now,
                    now.plusSeconds(expirationTimeSeconds),
                ),
            )
    }

    @Test
    fun `should throw InvalidTokenException when token is invalid`() {
        val invalidToken = Token("definitelyNotAToken")

        assertThatThrownBy { tokenReaderService.readToken(invalidToken) }
            .isInstanceOf(InvalidTokenException::class.java)
            .hasMessage("Token inválido: ${invalidToken.value}")
            .satisfies({
                assertThat(it.suppressed.size).isEqualTo(1)
                assertThat(it.suppressed[0]).isInstanceOf(JwtException::class.java)
            })
    }
}
