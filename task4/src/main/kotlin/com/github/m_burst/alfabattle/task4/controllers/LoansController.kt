package com.github.m_burst.alfabattle.task4.controllers

import com.github.m_burst.alfabattle.task4.dto.LoadResultDto
import com.github.m_burst.alfabattle.task4.dto.LoanDto
import com.github.m_burst.alfabattle.task4.dto.LoanHistoryDto
import com.github.m_burst.alfabattle.task4.dto.PersonDto
import com.github.m_burst.alfabattle.task4.service.LoanService
import com.github.m_burst.alfabattle.task4.service.PersonService
import org.springframework.web.bind.annotation.*

@RestController
class LoansController(
    private val personService: PersonService,
    private val loanService: LoanService
) {

    @PutMapping("/loans/loadPersons")
    fun loadPersons(): LoadResultDto {
        return LoadResultDto(status = "OK")
    }

    @PutMapping("/loans/loadLoans")
    fun loadLoans(): LoadResultDto {
        return LoadResultDto(status = "OK")
    }

    @GetMapping("/loans/getPerson/{documentId}/")
    fun getPerson(
        @PathVariable("documentId") documentId: String
    ): PersonDto {
        return PersonDto.of(personService.getPerson(documentId))
    }

    @GetMapping("/loans/getLoan/{loanId}")
    fun getLoan(
        @PathVariable("loanId") loanId: String
    ): LoanDto {
        return LoanDto.of(loanService.getLoan(loanId))
    }

    @GetMapping("/loans/creditHistory/{documentId}")
    fun getLoanHistory(
        @PathVariable("documentId") documentId: String
    ): LoanHistoryDto {
        return LoanHistoryDto.of(loanService.getLoanHistory(documentId))
    }
}
