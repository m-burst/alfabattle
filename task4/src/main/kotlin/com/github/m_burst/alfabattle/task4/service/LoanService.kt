package com.github.m_burst.alfabattle.task4.service

import com.github.m_burst.alfabattle.task4.domain.Loan
import com.github.m_burst.alfabattle.task4.persistence.LoanRepository
import org.springframework.stereotype.Service

class LoanNotFoundException : Exception()

@Service
class LoanService(
    private val loanRepository: LoanRepository
) {
    fun getLoan(loanId: String): Loan {
        return loanRepository.findById(loanId).orElseThrow { LoanNotFoundException() }
    }
}
