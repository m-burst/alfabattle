package com.github.m_burst.alfabattle.task1.dto

import com.github.m_burst.alfabattle.task1.providers.alfa.atm.model.ATMDetails

data class AtmDto(
    val deviceId: Int,
    val latitude: String?,
    val longitude: String?,
    val city: String?,
    val location: String?,
    val payments: Boolean
) {
    companion object {
        fun of(details: ATMDetails): AtmDto {
            return AtmDto(
                deviceId = details.deviceId,
                latitude = details.coordinates?.latitude,
                longitude = details.coordinates?.longitude,
                city = details.address?.city,
                location = details.address?.location,
                payments = details.services?.payments == "Y"
            )
        }
    }
}
