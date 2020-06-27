package com.github.m_burst.alfabattle.task2.services

import com.github.m_burst.alfabattle.task2.domain.Payment
import org.springframework.stereotype.Service
import java.math.BigDecimal

data class UserAnalytics(
    val userId: String,
    var totalSum: BigDecimal,
    val categoryAnalytics: MutableMap<Int, CategoryAnalytics>
) {
    operator fun plusAssign(payment: Payment) {
        totalSum += payment.amount
        categoryAnalytics.compute(payment.categoryId) { _, existing ->
            existing?.let { it += payment; it } ?: CategoryAnalytics.of(payment)
        }
    }

    companion object {
        fun of(payment: Payment): UserAnalytics {
            return UserAnalytics(
                userId = payment.userId,
                totalSum = payment.amount,
                categoryAnalytics = mutableMapOf(payment.categoryId to CategoryAnalytics.of(payment))
            )
        }
    }
}

data class CategoryAnalytics(
    var count: Int,
    var min: BigDecimal,
    var max: BigDecimal,
    var sum: BigDecimal
) {
    operator fun plusAssign(payment: Payment) {
        count++
        min = minOf(min, payment.amount)
        max = maxOf(max, payment.amount)
        sum += payment.amount
    }

    companion object {
        fun of(payment: Payment): CategoryAnalytics {
            return CategoryAnalytics(
                count = 1,
                min = payment.amount,
                max = payment.amount,
                sum = payment.amount
            )
        }
    }
}

data class UserCategoryStats(
    val oftenCategoryId: Int,
    val rareCategoryId: Int,
    val maxAmountCategoryId: Int,
    val minAmountCategoryId: Int
)

data class PaymentTemplate(
    val recipientId: String,
    val categoryId: Int,
    val amount: BigDecimal
) {
    companion object {
        fun of(payment: Payment): PaymentTemplate {
            return PaymentTemplate(
                recipientId = payment.recipientId,
                categoryId = payment.categoryId,
                amount = payment.amount
            )
        }
    }
}

class UserNotFoundException : Exception()

@Service
class AnalyticService {

    private val userIdToAnalytics = mutableMapOf<String, UserAnalytics>()
    private val userIdToTemplates = mutableMapOf<String, MutableMap<PaymentTemplate, Int>>()

    fun addPayment(payment: Payment) {
        userIdToAnalytics.compute(payment.userId) { _, existing ->
            existing?.let { it += payment; it } ?: UserAnalytics.of(payment)
        }

        val userTemplates = userIdToTemplates.computeIfAbsent(payment.userId) { mutableMapOf() }
        val template = PaymentTemplate.of(payment)
        userTemplates.compute(template) { _, currentCount ->
            (currentCount ?: 0) + 1
        }
    }

    fun getAllUserAnalytics(): List<UserAnalytics> {
        return userIdToAnalytics.values.toList()
    }

    fun getUserAnalytics(userId: String): UserAnalytics {
        return userIdToAnalytics[userId] ?: throw UserNotFoundException()
    }

    fun getUserCategoryStats(userId: String): UserCategoryStats {
        val categoryAnalytics = getUserAnalytics(userId).categoryAnalytics
        return UserCategoryStats(
            oftenCategoryId = categoryAnalytics.maxKey { it.count },
            rareCategoryId = categoryAnalytics.minKey { it.count },
            maxAmountCategoryId = categoryAnalytics.maxKey { it.sum },
            minAmountCategoryId = categoryAnalytics.minKey { it.sum }
        )
    }

    fun getUserTemplates(userId: String): List<PaymentTemplate> {
        val templateToCountMap = userIdToTemplates[userId] ?: throw UserNotFoundException()
        return templateToCountMap.mapNotNull { (template, count) ->
            if (count < 3) null else template
        }
    }

    private inline fun <K, V, S : Comparable<S>> Map<K, V>.maxKey(selector: (V) -> S): K {
        check(isNotEmpty()) { "Map is empty" }

        return entries.maxBy { (_, value) -> selector(value) }!!.key
    }

    private inline fun <K, V, S : Comparable<S>> Map<K, V>.minKey(selector: (V) -> S): K {
        check(isNotEmpty()) { "Map is empty" }

        return entries.minBy { (_, value) -> selector(value) }!!.key
    }
}
