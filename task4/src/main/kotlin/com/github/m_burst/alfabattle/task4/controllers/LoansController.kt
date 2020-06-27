package com.github.m_burst.alfabattle.task4.controllers

import com.github.m_burst.alfabattle.task4.dto.LoadResultDto
import com.github.m_burst.alfabattle.task4.dto.PersonDto
import com.github.m_burst.alfabattle.task4.service.PersonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoansController(
    private val personService: PersonService
) {

    @PostMapping("/loans/loadPersons")
    fun loadPersons(): LoadResultDto {
        return LoadResultDto(status = "OK")
    }

    @PostMapping("/loans/loadLoans")
    fun loadLoans(): LoadResultDto {
        return LoadResultDto(status = "OK")
    }

    @GetMapping("/loans/getPerson/{documentId}/")
    fun getPerson(
        @PathVariable("documentId") documentId: String
    ): PersonDto {
        return PersonDto.of(personService.getPerson(documentId))
    }
}
