package com.perquizz.perquizz.migrations.repository

import com.perquizz.perquizz.migrations.dtos.MigrationDto
import com.zaxxer.hikari.HikariDataSource
import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.command.core.StatusCommandStep
import liquibase.database.jvm.JdbcConnection
import liquibase.integration.spring.SpringLiquibase
import liquibase.integration.spring.SpringResourceAccessor
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Repository

@Repository
class MigrationRepositoryImpl(
    val liquibaseInfo: SpringLiquibase,
    val datasource: HikariDataSource,
    val resourceLoader: ResourceLoader,
) : MigrationRepository {
    override fun findPendingMigrations(): List<MigrationDto> {
        val liquibase = getLiquibase()

        val databaseChangeLog = liquibase.databaseChangeLog
        val database = liquibase.database
        val labels = LabelExpression(databaseChangeLog.includeLabels?.labels)
        val contexts = Contexts(databaseChangeLog.contextFilter?.contexts)
        val commandStep = StatusCommandStep()

        return commandStep
            .listUnrunChangeSets(contexts, labels, databaseChangeLog, database)
            .map { MigrationDto.from(it, database) }
    }

    private fun getLiquibase(): Liquibase {
        val connection = datasource.connection
        val database = JdbcConnection(connection)
        val resourceAccessor = SpringResourceAccessor(resourceLoader)
        return Liquibase(liquibaseInfo.changeLog, resourceAccessor, database)
    }
}
