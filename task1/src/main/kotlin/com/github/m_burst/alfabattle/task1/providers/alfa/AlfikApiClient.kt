package com.github.m_burst.alfabattle.task1.providers.alfa

import com.github.m_burst.alfabattle.util.ImmutableConfigurationProperties
import com.github.m_burst.alfabattle.util.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type

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
    private val stompClient: WebSocketStompClient,
    private val properties: AlfikApiProperties
) {
    private val log = logger<AlfikApiClient>()

    fun getAlfikCount(deviceId: Int): Long {
        val request = Request(deviceId = deviceId)
        val response = runBlocking {
            var received: Response? = null
            log.info("Connecting to ${properties.url}")
            val session = stompClient.connect(properties.url, MyStompSessionHandler { received = it }).completable().await()
            try {
                val destination = "/"
                log.info("Sending $request to $destination")
                // avoiding error I couldn't understand
                while (true) {
                    try {
                        session.send(destination, request)
                        break
                    } catch (e: IllegalStateException) {
                        log.warn("Try to send again", e)
                        delay(timeMillis = 100L)
                    }
                }

                withTimeout(timeMillis = properties.timeoutMillis) {
                    while (received == null) {
                        log.info("Waiting for response...")
                        delay(timeMillis = 100L)
                    }
                }
                received!!
            } finally {
                session.disconnect()
            }
        }
        return response.alfik
    }

    private inner class MyStompSessionHandler(
        private val callback: (Response) -> Unit
    ) : StompSessionHandler {

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
            session.subscribe("/topic/alfik", this)
        }

        override fun handleException(session: StompSession, command: StompCommand?, headers: StompHeaders, payload: ByteArray, exception: Throwable) {
            log.error("Got an exception", exception)
        }

        override fun handleTransportError(session: StompSession, exception: Throwable) {
            log.error("Got an exception", exception)
        }

        override fun getPayloadType(headers: StompHeaders): Type {
            return Response::class.java
        }

        override fun handleFrame(headers: StompHeaders, payload: Any?) {
            val msg = payload as Response?
            log.info("Received : $msg")
            if (msg != null) {
                callback(msg)
            }
        }
    }
}
