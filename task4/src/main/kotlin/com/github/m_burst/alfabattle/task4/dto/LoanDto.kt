package com.github.m_burst.alfabattle.task4.dto

import com.github.m_burst.alfabattle.task4.domain.Loan

data class LoanDto(
    val loan: String,
    val document: String,
    val amount: Double,
    val startdate: String,
    val period: Int
) {
    companion object {
        fun of(loan: Loan): LoanDto {
            return LoanDto(
                loan = loan.loan,
                document = loan.docid,
                amount = loan.amount,
                startdate = loan.startdate,
                period = loan.period
            )
        }
    }
}

data class LoanHistoryDto(
    val countLoans: Int,
    val sumAmountLoans: Double,
    val loans: List<LoanDto>
) {
    companion object {
        fun of(loans: List<Loan>): LoanHistoryDto {
            return LoanHistoryDto(
                countLoans = loans.size,
                sumAmountLoans = loans.sumByDouble { it.amount },
                loans = loans.map { LoanDto.of(it) }
            )
        }
    }
}
