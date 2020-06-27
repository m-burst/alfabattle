package com.github.m_burst.alfabattle.task4.controllers

import com.github.m_burst.alfabattle.task4.dto.ErrorDto
import com.github.m_burst.alfabattle.task4.service.LoanNotFoundException
import com.github.m_burst.alfabattle.task4.service.PersonNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(PersonNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePersonNotFound(e: PersonNotFoundException): ErrorDto {
        return ErrorDto("person not found")
    }

    @ExceptionHandler(LoanNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleLoanNotFound(e: LoanNotFoundException): ErrorDto {
        return ErrorDto("loan not found")
    }
}
