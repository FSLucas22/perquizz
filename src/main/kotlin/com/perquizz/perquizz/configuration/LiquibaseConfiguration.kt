package com.perquizz.perquizz.configuration

import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.integration.spring.SpringLiquibase
import liquibase.integration.spring.SpringResourceAccessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class LiquibaseConfiguration {
    @Bean("customLiquibase")
    fun liquibase(
        datasource: HikariDataSource,
        springLiquibase: SpringLiquibase,
        resourceLoader: ResourceLoader,
    ): Liquibase {
        val connection = datasource.connection
        val database = JdbcConnection(connection)
        val resourceAccessor = SpringResourceAccessor(resourceLoader)
        return Liquibase(springLiquibase.changeLog, resourceAccessor, database)
    }
}
