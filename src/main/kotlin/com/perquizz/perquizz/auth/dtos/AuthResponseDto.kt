package com.perquizz.perquizz.auth.dtos

import com.perquizz.perquizz.auth.valueobjects.Token

data class AuthResponseDto(val username: String, val token: Token)
