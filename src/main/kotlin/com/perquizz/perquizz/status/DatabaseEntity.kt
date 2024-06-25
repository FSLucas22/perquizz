package com.perquizz.perquizz.status

import jakarta.persistence.Query
import org.hibernate.Session


data class StatActivityEntity(
    val usename: String?,
    val applicationName: String?,
    val clientAddr: String?,
    val clientHostname: String?,
    val clientPort: String?,
    val query: String?,
)

data class StatusEntity(
    val version: String,
    val maxConnections: Int,
    val activeConnections: Int,
    val statActivity: List<StatActivityEntity>,
) {
    companion object {
        fun getStatus(session: Session): StatusEntity {
            val version = getVersion(session)
            val maxConnections = getMaxConnections(session)
            val activeConnections = getActiveConnections(session)
            val statActivity = getStatActivity(session)
            return StatusEntity(version, maxConnections, activeConnections, statActivity)
        }

        fun getVersion(session: Session): String {
            val query: Query = session.createNativeQuery(
                "SHOW server_version",
                String::class.java
            )
            return query.singleResult as String
        }

        fun getMaxConnections(session: Session): Int {
            val query: Query = session.createNativeQuery(
                "SHOW max_connections",
                Int::class.java
            )
            return query.singleResult as Int
        }

        fun getActiveConnections(session: Session): Int {
            val query: Query = session.createNativeQuery(
                "select count(1)::int as used_connections from pg_stat_activity where datname = ?1;",
                Int::class.java
            )
            query.setParameter(1, "local_db")
            return query.singleResult as Int
        }

        fun getStatActivity(session: Session): List<StatActivityEntity> {
            val query: Query = session.createNativeQuery(
                "select usename::text, application_name::text, client_addr::text, client_hostname::text, client_port::text, query::text from pg_stat_activity where datname = ?1;",
                StatActivityEntity::class.java
            )
            query.setParameter(1, "local_db")
            return query.resultList as List<StatActivityEntity>
        }
    }
}