package com.perquizz.perquizz.status

import jakarta.persistence.EntityManager
import jakarta.persistence.Query

data class DatabaseEntity(
    val version: String,
    val maxConnections: Int,
    val activeConnections: Int,
) {
    fun toDto(): DatabaseDto {
        return DatabaseDto(version, maxConnections, activeConnections)
    }

    companion object {
        fun fromEntityManager(
            entityManager: EntityManager,
            databaseName: String,
        ): DatabaseEntity {
            val version = getVersion(entityManager)
            val maxConnections = getMaxConnections(entityManager)
            val activeConnections = getActiveConnections(entityManager, databaseName)
            return DatabaseEntity(version, maxConnections, activeConnections)
        }

        fun getVersion(entityManager: EntityManager): String {
            val query: Query =
                entityManager.createNativeQuery(
                    "SHOW server_version",
                    String::class.java,
                )
            return query.singleResult as String
        }

        fun getMaxConnections(entityManager: EntityManager): Int {
            val query: Query =
                entityManager.createNativeQuery(
                    "SHOW max_connections",
                    Int::class.java,
                )
            return query.singleResult as Int
        }

        fun getActiveConnections(
            entityManager: EntityManager,
            databaseName: String,
        ): Int {
            val query: Query =
                entityManager.createNativeQuery(
                    "select count(1)::int as used_connections from pg_stat_activity where datname = ?1;",
                    Int::class.java,
                )
            query.setParameter(1, databaseName)
            return query.singleResult as Int
        }
    }
}
