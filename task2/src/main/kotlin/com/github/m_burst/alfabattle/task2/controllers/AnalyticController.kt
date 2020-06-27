package com.github.m_burst.alfabattle.task2.controllers

import com.github.m_burst.alfabattle.task2.dto.UserAnalyticsDto
import com.github.m_burst.alfabattle.task2.services.AnalyticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalyticController(
    private val analyticService: AnalyticService
) {

    @GetMapping("/analytic")
    fun list(): List<UserAnalyticsDto> {
        val analytics = analyticService.getAllUserAnalytics()
        return analytics.map { UserAnalyticsDto.of(it) }
    }

    @GetMapping("/analytic/{userId}")
    fun get(
        @PathVariable("userId") userId: String
    ): UserAnalyticsDto {
        return UserAnalyticsDto.of(analyticService.getUserAnalytics(userId))
    }
}
