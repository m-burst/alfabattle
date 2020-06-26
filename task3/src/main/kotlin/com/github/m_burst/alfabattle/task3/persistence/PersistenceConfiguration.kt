package com.github.m_burst.alfabattle.task3.persistence

import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class PersistenceConfiguration {

    @Bean
    fun exposedDatabase(
        dataSource: DataSource
    ): Database = Database.connect(dataSource)
}
