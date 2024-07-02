package com.perquizz.perquizz.migrations.controllers

import com.perquizz.perquizz.migrations.dtos.MigrationsResponseDto
import com.perquizz.perquizz.migrations.services.MigrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class MigrationController(val service: MigrationService) {
    @GetMapping("/migrations")
    fun getMigrations(): ResponseEntity<MigrationsResponseDto> =
        ResponseEntity.ok(service.getPendingMigrations())

    @PostMapping("/migrations")
    fun doMigrations(): ResponseEntity<MigrationsResponseDto> = ResponseEntity.ok(service.migrate())
}
