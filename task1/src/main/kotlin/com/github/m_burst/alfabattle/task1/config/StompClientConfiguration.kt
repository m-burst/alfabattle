package com.github.m_burst.alfabattle.task1.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.web.socket.client.jetty.JettyWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient

@Configuration
@EnableIntegration
class StompClientConfiguration {

    @Bean
    fun stompClient(
        objectMapper: ObjectMapper
    ): WebSocketStompClient {
        val websocketClient = JettyWebSocketClient()
        val stompClient = WebSocketStompClient(websocketClient)
        stompClient.messageConverter = MappingJackson2MessageConverter().also { it.objectMapper = objectMapper }
        return stompClient
    }
}
