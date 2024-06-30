package com.perquizz.perquizz.migrations.dtos

data class ChangeDto(val description: String, val errors: List<String>, val warnings: List<String>)
