package com.perquizz.perquizz.status.dtos

data class DatabaseDto(val version: String, val maxConnections: Int, val activeConnections: Int)
