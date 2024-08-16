package com.perquizz.perquizz.exceptions.dtos

data class ErrorResponseDto(val type: String, val message: String, val status: Int)
