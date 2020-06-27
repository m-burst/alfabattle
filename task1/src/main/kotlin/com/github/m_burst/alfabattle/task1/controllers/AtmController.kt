package com.github.m_burst.alfabattle.task1.controllers

import com.github.m_burst.alfabattle.task1.dto.AtmDto
import com.github.m_burst.alfabattle.task1.services.AtmService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

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
}
