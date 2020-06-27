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

class UserNotFoundException : Exception()

@Service
class AnalyticService {

    private val userIdToAnalytics = mutableMapOf<String, UserAnalytics>()

    fun addPayment(payment: Payment) {
        userIdToAnalytics.compute(payment.userId) { _, existing ->
            existing?.let { it += payment; it } ?: UserAnalytics.of(payment)
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

    private inline fun <K, V, S : Comparable<S>> Map<K, V>.maxKey(selector: (V) -> S): K {
        check(isNotEmpty()) { "Map is empty" }

        return entries.maxBy { (_, value) -> selector(value) }!!.key
    }

    private inline fun <K, V, S : Comparable<S>> Map<K, V>.minKey(selector: (V) -> S): K {
        check(isNotEmpty()) { "Map is empty" }

        return entries.minBy { (_, value) -> selector(value) }!!.key
    }
}
