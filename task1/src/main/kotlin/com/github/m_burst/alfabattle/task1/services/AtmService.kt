package com.github.m_burst.alfabattle.task1.services

import com.github.m_burst.alfabattle.task1.providers.alfa.AlfaApiClient
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.ATMDetails
import org.springframework.stereotype.Service

class AtmNotFoundException : Exception()

@Service
class AtmService(
    private val alfaApiClient: AlfaApiClient
) {

    private val idToAtmMap by lazy(::loadAtmData)

    fun getAtmDetails(id: Int): ATMDetails {
        return idToAtmMap[id] ?: throw AtmNotFoundException()
    }

    private fun loadAtmData(): Map<Int, ATMDetails> {
        val response = alfaApiClient.atmsGet().execute()
        check(response.isSuccessful)
        val body = response.body()
        check(body != null && body.success == true && body.data != null)
        return body.data.atms.associateBy { it.deviceId }
    }
}
