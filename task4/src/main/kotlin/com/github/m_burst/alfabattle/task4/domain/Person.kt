package com.github.m_burst.alfabattle.task4.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "person", type = "Person", createIndex = true)
data class Person(
    @Id
    val id: String,
    val docid: String,
    val fio: String,
    val birthday: String,
    val salary: Double,
    val gender: String
)
