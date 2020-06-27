package com.github.m_burst.alfabattle.task2.controllers

import com.github.m_burst.alfabattle.task2.dto.HealthDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {

    @GetMapping("/admin/health")
    fun health(): HealthDto {
        return HealthDto(status = "UP")
    }
}
