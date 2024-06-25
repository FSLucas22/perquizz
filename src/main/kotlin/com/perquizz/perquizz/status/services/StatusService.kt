package com.perquizz.perquizz.status.services

import com.perquizz.perquizz.status.dtos.StatusResponseDto

interface StatusService {
    fun getStatus(): StatusResponseDto
}
