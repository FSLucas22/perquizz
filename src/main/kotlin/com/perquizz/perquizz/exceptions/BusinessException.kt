package com.perquizz.perquizz.exceptions

import com.perquizz.perquizz.exceptions.dtos.ErrorResponseDto
import org.springframework.http.HttpStatus

class BusinessException(
    val type: String,
    override val message: String,
    val status: HttpStatus,
) : RuntimeException(message) {
    fun toDto(): ErrorResponseDto = ErrorResponseDto(type, message, status.value())
}
