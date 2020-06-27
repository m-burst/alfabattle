package com.github.m_burst.alfabattle.task2.domain

import java.math.BigDecimal

data class Payment(
    val ref: String,
    val categoryId: Int,
    val userId: String,
    val recipientId: String,
    val desc: String,
    val amount: BigDecimal
)
