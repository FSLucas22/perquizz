package com.perquizz.perquizz.users.controllers

import com.perquizz.perquizz.users.dtos.CreateUserRequestDto
import com.perquizz.perquizz.users.dtos.UserDetailsDto
import com.perquizz.perquizz.users.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(
    private val service: UserService,
) {
    @PostMapping("/api/v1/user")
    fun createUser(
        @RequestBody request: CreateUserRequestDto,
    ): ResponseEntity<UserDetailsDto> =
        service.createUser(request).let {
            ResponseEntity.created(URI.create("/api/v1/user/" + it.id))
                .body(it)
        }

    @GetMapping("/api/v1/user/{userId}")
    fun findUserById(
        @PathVariable userId: Long,
    ): ResponseEntity<UserDetailsDto> =
        service.findUserById(userId).let {
            ResponseEntity.ok().body(it)
        }
}
