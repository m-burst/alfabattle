package com.github.m_burst.alfabattle.task1.controllers

import com.github.m_burst.alfabattle.task1.dto.ErrorDto
import com.github.m_burst.alfabattle.task1.services.AtmNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(AtmNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAtmNotFound(e: AtmNotFoundException): ErrorDto {
        return ErrorDto("atm not found")
    }
}