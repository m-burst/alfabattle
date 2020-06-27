package com.github.m_burst.alfabattle.task4.persistence

import com.github.m_burst.alfabattle.task4.domain.Loan
import com.github.m_burst.alfabattle.task4.domain.Person
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface PersonRepository : ElasticsearchRepository<Person, String> {
    fun findByDocid(docid: String): Person?
}

interface LoanRepository : ElasticsearchRepository<Loan, String> {
    fun findAllByDocid(docid: String): List<Loan>
}
