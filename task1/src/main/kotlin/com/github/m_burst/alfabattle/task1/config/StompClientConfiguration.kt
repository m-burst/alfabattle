package com.github.m_burst.alfabattle.task1.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.m_burst.alfabattle.util.StompSessionFactory
import org.hildan.krossbow.stomp.StompClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StompClientConfiguration {

    @Bean
    fun stompClient(): StompClient = StompClient()

    @Bean
    fun stompSessionFactory(
        stompClient: StompClient,
        objectMapper: ObjectMapper
    ): StompSessionFactory = StompSessionFactory(stompClient, objectMapper)
}
