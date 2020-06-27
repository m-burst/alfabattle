package com.github.m_burst.alfabattle.task4.controllers

import com.github.m_burst.alfabattle.task4.dto.ErrorDto
import com.github.m_burst.alfabattle.task4.service.PersonNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(PersonNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAtmNotFound(e: PersonNotFoundException): ErrorDto {
        return ErrorDto("user not found")
    }
}
