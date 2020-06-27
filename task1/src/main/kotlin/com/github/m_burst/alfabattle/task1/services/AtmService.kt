package com.github.m_burst.alfabattle.task1.services

import com.github.m_burst.alfabattle.task1.providers.alfa.AlfaApiClient
import com.github.m_burst.alfabattle.task1.providers.alfa.AlfikApiClient
import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.ATMDetails
import org.springframework.stereotype.Service
import java.math.BigDecimal

class AtmNotFoundException : Exception()

@Service
class AtmService(
    private val alfaApiClient: AlfaApiClient,
    private val alfikApiClient: AlfikApiClient
) {

    private val atmList by lazy(::loadAtmData)
    private val atmListWithPayments by lazy {
        atmList.filter { it.hasPayments }
    }
    private val idToAtmMap by lazy {
        atmList.associateBy { it.deviceId }
    }

    fun getAtmDetails(id: Int): ATMDetails {
        return idToAtmMap[id] ?: throw AtmNotFoundException()
    }

    fun findNearest(latitude: BigDecimal, longitude: BigDecimal, onlyWithPayments: Boolean): ATMDetails {
        val listToSearch =
            if (onlyWithPayments) atmListWithPayments
            else atmList

        return listToSearch.minBy { it.distanceSquared(latitude, longitude) }
            ?: throw AtmNotFoundException()
    }

    fun findNearestWithAlfik(latitude: BigDecimal, longitude: BigDecimal, alfikCount: Long): List<ATMDetails> {
        val sortedByDistance = atmList.sortedBy { it.distanceSquared(latitude, longitude) }
        var atmCount = 0
        var remainingAlfiks = alfikCount
        while (remainingAlfiks > 0) {
            atmCount++
            val currentAtm = sortedByDistance[atmCount - 1]
            remainingAlfiks -= alfikApiClient.getAlfikCount(currentAtm.deviceId)
        }
        return sortedByDistance.take(atmCount)
    }

    private fun loadAtmData(): List<ATMDetails> {
        val response = alfaApiClient.atmsGet().execute()
        check(response.isSuccessful)
        val body = response.body()
        check(body != null && body.success == true && body.data != null)
        return body.data.atms.toList()
    }

    companion object {
        private val INFINITY: BigDecimal = BigDecimal.valueOf(1, -100)

        private val ATMDetails.hasPayments: Boolean
            get() = services?.payments == "Y"

        private fun ATMDetails.distanceSquared(latitude: BigDecimal, longitude: BigDecimal): BigDecimal {
            // if atm doesn't have latitude or longitude, it cannot be nearest
            val atmLatitude = coordinates?.latitude?.toBigDecimal() ?: return INFINITY
            val atmLongitude = coordinates.longitude?.toBigDecimal() ?: return INFINITY

            return (latitude - atmLatitude).pow(2) + (longitude - atmLongitude).pow(2)
        }
    }
}
