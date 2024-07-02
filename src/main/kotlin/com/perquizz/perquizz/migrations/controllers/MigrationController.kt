package com.perquizz.perquizz.migrations.controllers

import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.services.MigrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MigrationController(val service: MigrationService) {
    @GetMapping("/api/v1/migrations")
    fun getMigrations(): ResponseEntity<MigrationsResponseDto> =
        ResponseEntity.ok(service.getPendingMigrations())

    @PostMapping("/api/v1/migrations")
    fun doMigrations(): ResponseEntity<MigrationsResponseDto> = ResponseEntity.ok(service.migrate())
}
