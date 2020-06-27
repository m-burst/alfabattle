package com.github.m_burst.alfabattle.task2.controllers

import com.github.m_burst.alfabattle.task2.dto.ErrorDto
import com.github.m_burst.alfabattle.task2.services.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAtmNotFound(e: UserNotFoundException): ErrorDto {
        return ErrorDto("user not found")
    }
}
