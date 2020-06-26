package com.github.m_burst.alfabattle.task1.providers.alfa

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AlfaApiProperties::class)
class AlfaApiConfiguration {

    @Bean
    fun alfaApiClient(
        objectMapper: ObjectMapper,
        alfaApiProperties: AlfaApiProperties
    ): AlfaApiClient = AlfaApiClient.create(properties = alfaApiProperties, objectMapper = objectMapper)
}
