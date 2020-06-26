package com.github.m_burst.alfabattle.task4.config

import com.github.m_burst.alfabattle.util.ImmutableConfigurationProperties
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration

@ImmutableConfigurationProperties("application.elasticsearch")
data class ElasticsearchProperties(
    val hosts: List<String>
)

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties::class)
class ElasticsearchConfiguration(
    private val properties: ElasticsearchProperties
) : AbstractElasticsearchConfiguration() {

    @Bean
    override fun elasticsearchClient(): RestHighLevelClient {
        val clientConfiguration = ClientConfiguration.builder()
            .connectedTo(*properties.hosts.toTypedArray())
            .build()

        return RestClients.create(clientConfiguration).rest()
    }
}
