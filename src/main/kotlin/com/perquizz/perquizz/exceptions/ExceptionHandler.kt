package com.perquizz.perquizz.exceptions

import com.perquizz.perquizz.exceptions.dtos.ErrorResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(exception.status).body(exception.toDto())
    }
}
