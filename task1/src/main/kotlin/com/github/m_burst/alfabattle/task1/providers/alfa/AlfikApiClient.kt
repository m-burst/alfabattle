package com.github.m_burst.alfabattle.task1.providers.alfa

import com.github.m_burst.alfabattle.util.ImmutableConfigurationProperties
import com.github.m_burst.alfabattle.util.StompSessionFactory
import com.github.m_burst.alfabattle.util.logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hildan.krossbow.stomp.conversions.convertAndSend
import org.hildan.krossbow.stomp.conversions.subscribe
import org.hildan.krossbow.stomp.use
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration

@ImmutableConfigurationProperties("application.alfik-api")
data class AlfikApiProperties(
    val url: String,
    val timeoutMillis: Long = 10000
)

data class Request(val deviceId: Int)
data class Response(val deviceId: Int, val alfik: Long)

@Component
@EnableConfigurationProperties(AlfikApiProperties::class)
class AlfikApiClient(
    private val stompSessionFactory: StompSessionFactory,
    private val properties: AlfikApiProperties
) {
    private val log = logger<AlfikApiClient>()

    fun getAlfikCount(deviceId: Int): Long {
        val request = Request(deviceId = deviceId)
        val response = runBlocking {
            log.info("Connecting to ${properties.url}")
            val session = stompSessionFactory.newSession(properties.url)
            session.use {
                val destination = "/"
                log.info("Sending $request to $destination")
                convertAndSend(destination, request)

                val subscription = subscribe<Response>("/topic/alfik")
                withTimeout(timeMillis = properties.timeoutMillis) {
                    subscription.first()
                }
            }
        }
        return response.alfik
    }
}
