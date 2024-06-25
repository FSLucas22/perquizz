package com.perquizz.perquizz.status

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class StatusController(
    @Autowired val entityManager: EntityManager) {
    @GetMapping("/api/v1/status")
    fun returnStatus(): ResponseEntity<StatusResponseDto> {
        val databaseDto = DatabaseEntity.fromEntityManager(entityManager).toDto()
        val response = StatusResponseDto(
            DependenciesDto(databaseDto))
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}