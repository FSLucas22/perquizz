package com.perquizz.perquizz.integration

import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.Scope
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.visitor.ChangeExecListener
import liquibase.command.CommandScope
import liquibase.command.core.UpdateCommandStep
import liquibase.command.core.helpers.ChangeExecListenerCommandStep
import liquibase.command.core.helpers.DatabaseChangelogCommandStep
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep
import liquibase.resource.ClassLoaderResourceAccessor
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MigrationControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var liquibase: Liquibase

    @Autowired
    lateinit var dataSource: HikariDataSource

    @Test
    fun `should return status 200 and one migrations list`() {
        liquibase.dropAll()
        val result = mockMvc.perform(get("/api/v1/migrations"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(1)),
        )
    }

    @Test
    fun `should return status 200 and empty migrations list`() {
        migrateDb()
        val result = mockMvc.perform(get("/api/v1/migrations"))
        result.andExpectAll(
            status().isOk,
            jsonPath("$.migrations").isArray,
            jsonPath("$.migrations", hasSize<Int>(0)),
        )
    }

    private fun migrateDb() {
        dataSource.connection.use { connection ->
            val database = liquibase.database
            val changeLogFile = liquibase.databaseChangeLog.physicalFilePath

            val scopeObjects =
                mapOf(
                    Scope.Attr.database.name to database,
                    Scope.Attr.resourceAccessor.name to ClassLoaderResourceAccessor(),
                )

            Scope.child(scopeObjects) {
                val updateCommand =
                    CommandScope(*UpdateCommandStep.COMMAND_NAME).apply {
                        addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database)
                        addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changeLogFile)
                        addArgumentValue(
                            ChangeExecListenerCommandStep.CHANGE_EXEC_LISTENER_ARG,
                            null as ChangeExecListener?,
                        )
                        addArgumentValue(
                            DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS,
                            ChangeLogParameters(database),
                        )
                    }
                updateCommand.execute()
            }
        }
    }
}
