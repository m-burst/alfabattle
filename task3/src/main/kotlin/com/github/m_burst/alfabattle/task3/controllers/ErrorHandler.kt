package com.github.m_burst.alfabattle.task3.controllers

import com.github.m_burst.alfabattle.task3.dto.ErrorDto
import com.github.m_burst.alfabattle.task3.services.BranchNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(BranchNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleAtmNotFound(e: BranchNotFoundException): ErrorDto {
        return ErrorDto("branch not found")
    }
}
