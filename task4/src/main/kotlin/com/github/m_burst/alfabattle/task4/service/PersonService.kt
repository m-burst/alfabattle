package com.github.m_burst.alfabattle.task4.service

import com.github.m_burst.alfabattle.task4.domain.Person
import com.github.m_burst.alfabattle.task4.persistence.PersonRepository
import org.springframework.stereotype.Service

class PersonNotFoundException : Exception()

@Service
class PersonService(
    private val personRepository: PersonRepository
) {

    fun getPerson(documentId: String): Person {
        return personRepository.findByDocid(docid = documentId)
            ?: throw PersonNotFoundException()
    }
}
