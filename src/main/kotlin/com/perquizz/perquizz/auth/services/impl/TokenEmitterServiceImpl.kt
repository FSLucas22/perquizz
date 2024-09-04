package com.perquizz.perquizz.auth.services.impl

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.TokenEmitterService
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.configuration.JwtConfiguration
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class TokenEmitterServiceImpl(
    private val jwtConfiguration: JwtConfiguration,
) : TokenEmitterService {
    override fun issueToken(
        request: TokenRequestDto,
        issueInstant: Instant,
    ): Token {
        val alg = Jwts.KEY.PBES2_HS512_A256KW

        val enc = Jwts.ENC.A256GCM

        return Token(
            Jwts
                .builder()
                .encryptWith(jwtConfiguration.password, alg, enc)
                .issuedAt(Date.from(issueInstant))
                .expiration(Date.from(issueInstant.plusSeconds(jwtConfiguration.secondsToExpire)))
                .subject(request.userId.toString())
                .compact(),
        )
    }
}
