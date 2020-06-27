package com.github.m_burst.alfabattle.task4.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "loan", type = "Loan", createIndex = true)
data class Loan(
    @Id
    val loan: String,
    val docid: String,
    val amount: Double,
    val startdate: String,
    val period: Int
)
