package com.github.m_burst.alfabattle.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.StompSessionWithClassConversions
import org.hildan.krossbow.stomp.conversions.withJacksonConversions

class StompSessionFactory(
    private val stompClient: StompClient,
    private val objectMapper: ObjectMapper
) {
    suspend fun newSession(url: String): StompSessionWithClassConversions {
        return stompClient.connect(url).withJacksonConversions(objectMapper = objectMapper)
    }
}
