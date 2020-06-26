package com.github.m_burst.alfabattle.task1.providers

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

@ImmutableConfigurationProperties("application.alfa-stomp-api")
data class AlfaStompApiProperties(
    val url: String
)

data class Request(val content: String)
data class Response(val superContent: String)

@Component
@EnableConfigurationProperties(AlfaStompApiProperties::class)
class AlfaStompApiClient(
    private val stompSessionFactory: StompSessionFactory,
    private val properties: AlfaStompApiProperties
) {
    private val log = logger<AlfaStompApiClient>()

    fun getResponse(request: Request, timeout: Duration): Response {
        return runBlocking {
            val session = stompSessionFactory.newSession(properties.url)
            session.use {
                log.info("Sending $request")
                convertAndSend("/app/message", request)

                val subscription = subscribe<Response>("/topic/messages")
                withTimeout(timeMillis = timeout.toMillis()) {
                    subscription.first()
                }
            }
        }
    }
}
