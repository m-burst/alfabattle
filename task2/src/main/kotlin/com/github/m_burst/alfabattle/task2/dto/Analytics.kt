package com.github.m_burst.alfabattle.task2.dto

import com.github.m_burst.alfabattle.task2.services.CategoryAnalytics
import com.github.m_burst.alfabattle.task2.services.PaymentTemplate
import com.github.m_burst.alfabattle.task2.services.UserAnalytics
import com.github.m_burst.alfabattle.task2.services.UserCategoryStats

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

data class UserCategoryStatsDto(
    val oftenCategoryId: Int,
    val rareCategoryId: Int,
    val maxAmountCategoryId: Int,
    val minAmountCategoryId: Int
) {
    companion object {
        fun of(stats: UserCategoryStats): UserCategoryStatsDto {
            return UserCategoryStatsDto(
                oftenCategoryId = stats.oftenCategoryId,
                rareCategoryId = stats.rareCategoryId,
                maxAmountCategoryId = stats.maxAmountCategoryId,
                minAmountCategoryId = stats.minAmountCategoryId
            )
        }
    }
}

data class PaymentTemplateDto(
    val recipientId: String,
    val categoryId: Int,
    val amount: Double
) {
    companion object {
        fun of(paymentTemplate: PaymentTemplate): PaymentTemplateDto {
            return PaymentTemplateDto(
                recipientId = paymentTemplate.recipientId,
                categoryId = paymentTemplate.categoryId,
                amount = paymentTemplate.amount.toDouble()
            )
        }
    }
}
