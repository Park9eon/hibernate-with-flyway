package com.example.book

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@DataJpaTest(
    showSql = true,
    properties = [
        "spring.jpa.properties.javax.persistence.schema-generation.database.action=validate",
        "spring.jpa.properties.javax.persistence.schema-generation.scripts.action=update",
        "spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=src/main/resources/db/migration/update.sql",
        "spring.jpa.properties.javax.persistence.schema-generation.scripts.create-source=metadata",
        "spring.jpa.properties.hibernate.hbm2ddl.delimiter=;",
        "spring.jpa.database=postgresql",
        "spring.flyway.enabled=true",
   ],
)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(
    classes = [BookConfiguration::class],
    initializers = [SchemaGenerateTests.Initializer::class],
)
class SchemaGenerateTests {

    companion object {
        @JvmStatic
        @Container
        val container: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:latest")
    }

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.driver-class-name=${container.driverClassName}",
                "spring.datasource.url=${container.jdbcUrl}",
                "spring.datasource.username=${container.username}",
                "spring.datasource.password=${container.password}"
            ).applyTo(applicationContext.environment)
        }
    }

    @Test
    fun `test generate create sql`() {

    }

}