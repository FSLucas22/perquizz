package com.perquizz.perquizz.auth.services

import com.perquizz.perquizz.auth.dtos.TokenDto
import com.perquizz.perquizz.auth.valueobjects.Token

interface TokenReaderService {
    fun readToken(token: Token): TokenDto
}
