package com.github.m_burst.alfabattle.task4.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.m_burst.alfabattle.task4.domain.Loan
import com.github.m_burst.alfabattle.task4.domain.Person
import com.github.m_burst.alfabattle.util.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

private data class PersonDto(
    val ID: String,
    val DocId: String,
    val FIO: String,
    val Birthday: String,
    val Salary: String,
    val Gender: String
)

private data class PersonFile(
    val persons: List<PersonDto>
)

private data class LoanDto(
    val Loan: String,
    val PersonId: String,
    val Amount: String,
    val StartDate: String,
    val Period: String
)

private data class LoanFile(
    val loans: List<LoanDto>
)

@Component
@ConditionalOnProperty("application.data-loader.enabled", havingValue = "true")
class DataLoader(
    private val personRepository: PersonRepository,
    private val loanRepository: LoanRepository,
    private val objectMapper: ObjectMapper
) : ApplicationListener<ContextRefreshedEvent> {

    private val log = logger<DataLoader>()

    private val srcDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
    private val dstDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("DataLoader starting")

        loadPeople()
        loadLoans()

        log.info("DataLoader completed")
    }

    private fun loadPeople() {
        val resourceName = "persons.json"
        val resourceStream = this::class.java.classLoader.getResourceAsStream(resourceName)
        checkNotNull(resourceStream) { "Could not open resource '$resourceName'" }
        val data = resourceStream.use {
            objectMapper.readValue<PersonFile>(it).persons
        }
        personRepository.saveAll(data.map { it.toPerson() })
    }

    private fun loadLoans() {
        val resourceName = "loans.json"
        val resourceStream = this::class.java.classLoader.getResourceAsStream(resourceName)
        checkNotNull(resourceStream) { "Could not open resource '$resourceName'" }
        val data = resourceStream.use {
            objectMapper.readValue<LoanFile>(it).loans
        }
        loanRepository.saveAll(data.map { it.toLoan() })
    }

    private fun PersonDto.toPerson(): Person {
        return Person(
            id = ID,
            docid = DocId,
            fio = FIO,
            birthday = convertDate(Birthday),
            salary = Salary.toDouble() * 100,
            gender = Gender
        )
    }

    private fun LoanDto.toLoan(): Loan {
        val document = personRepository.findById(PersonId).get().docid
        return Loan(
            loan = Loan,
            docid = document,
            amount = Amount.toDouble() * 100,
            startdate = convertDate(StartDate),
            period = Period.toInt() * 12
        )
    }

    private fun convertDate(date: String): String {
        val parsed = srcDateFormatter.parse(date)
        return dstDateFormatter.format(parsed)
    }
}
