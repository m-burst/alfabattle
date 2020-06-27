package com.github.m_burst.alfabattle.task2.dto

import com.github.m_burst.alfabattle.task2.services.CategoryAnalytics
import com.github.m_burst.alfabattle.task2.services.UserAnalytics

data class UserAnalyticsDto(
    val userId: String,
    val totalSum: Double,
    val analyticInfo: Map<String, CategoryAnalyticsDto>
) {
    companion object {
        fun of(userAnalytics: UserAnalytics): UserAnalyticsDto {
            return UserAnalyticsDto(
                userId = userAnalytics.userId,
                totalSum = userAnalytics.totalSum.toDouble(),
                analyticInfo = userAnalytics.categoryAnalytics.entries.associate { (key, value) ->
                    key.toString() to CategoryAnalyticsDto.of(value)
                }
            )
        }
    }
}

data class CategoryAnalyticsDto(
    val min: Double,
    val max: Double,
    val sum: Double
) {
    companion object {
        fun of(categoryAnalytics: CategoryAnalytics): CategoryAnalyticsDto {
            return CategoryAnalyticsDto(
                min = categoryAnalytics.min.toDouble(),
                max = categoryAnalytics.max.toDouble(),
                sum = categoryAnalytics.sum.toDouble()
            )
        }
    }
}
