package com.perquizz.perquizz.status

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class StatusController {
    @GetMapping("/api/v1/status")
    fun returnStatus(): ResponseEntity<StatusResponseDto> {
        val response = StatusResponseDto(
            DependenciesDto(
                DatabaseDto("16.0", 100, 1)))
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}