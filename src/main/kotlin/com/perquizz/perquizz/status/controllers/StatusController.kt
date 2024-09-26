package com.perquizz.perquizz.status.controllers

import com.perquizz.perquizz.status.dtos.StatusResponseDto
import com.perquizz.perquizz.status.services.StatusService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController(private val service: StatusService) {
    @GetMapping("/api/v1/status")
    fun returnStatus(): ResponseEntity<StatusResponseDto> {
        val response = service.getStatus()
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}
