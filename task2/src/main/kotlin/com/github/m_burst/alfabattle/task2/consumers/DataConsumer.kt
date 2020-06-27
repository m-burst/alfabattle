package com.github.m_burst.alfabattle.task2.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.m_burst.alfabattle.task2.domain.Payment
import com.github.m_burst.alfabattle.task2.services.AnalyticService
import com.github.m_burst.alfabattle.util.logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component



@Component
class DataConsumer(
    private val analyticService: AnalyticService,
    private val objectMapper: ObjectMapper
) {

    val log = logger<DataConsumer>()

    @KafkaListener(topics = ["RAW_PAYMENTS"])
    fun onKafkaMessage(message: String) {
        log.info("Received: $message")

        val payment = try {
            objectMapper.readValue<Payment>(message)
        } catch (e: Exception) {
            log.warn("Invalid message: $message")
            return
        }

        analyticService.addPayment(payment)
    }
}
