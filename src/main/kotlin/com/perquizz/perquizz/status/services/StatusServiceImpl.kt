package com.perquizz.perquizz.status.services

import com.perquizz.perquizz.status.dtos.DatabaseDto
import com.perquizz.perquizz.status.dtos.DependenciesDto
import com.perquizz.perquizz.status.dtos.StatusResponseDto
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StatusServiceImpl(
    val entityManager: EntityManager,
    @Value("\${db.name}") val databaseName: String,
) : StatusService {
    override fun getStatus(): StatusResponseDto {
        val databaseDto = DatabaseDto(getVersion(), getMaxConnections(), getActiveConnections())
        return StatusResponseDto(DependenciesDto(databaseDto))
    }

    fun getVersion(): String =
        entityManager.createNativeQuery(
            "SHOW server_version",
            String::class.java,
        ).singleResult as String

    fun getMaxConnections(): Int =
        entityManager.createNativeQuery(
            "SHOW max_connections",
            Int::class.java,
        ).singleResult as Int

    fun getActiveConnections() =
        entityManager.createNativeQuery(
            "select count(1)::int as used_connections from pg_stat_activity where datname = ?1;",
            Int::class.java,
        ).setParameter(1, databaseName).singleResult as Int
}
