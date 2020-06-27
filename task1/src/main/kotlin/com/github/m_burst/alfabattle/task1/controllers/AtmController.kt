package com.github.m_burst.alfabattle.task1.controllers

import com.github.m_burst.alfabattle.task1.dto.AtmDto
import com.github.m_burst.alfabattle.task1.services.AtmService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class AtmController(
    private val atmService: AtmService
) {

    @GetMapping("/atms/{id}")
    fun get(
        @PathVariable("id") id: Int
    ): AtmDto {
        return AtmDto.of(atmService.getAtmDetails(id))
    }

    @GetMapping("/atms/nearest")
    fun findNearest(
        @RequestParam("latitude") latitude: BigDecimal,
        @RequestParam("longitude") longitude: BigDecimal,
        @RequestParam("payments") payments: Boolean = false
    ): AtmDto {
        val atmDetails = atmService.findNearest(
            latitude = latitude,
            longitude = longitude,
            onlyWithPayments = payments
        )
        return AtmDto.of(atmDetails)
    }
}
