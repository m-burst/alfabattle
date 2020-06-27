package com.github.m_burst.alfabattle.task3.services

import com.github.m_burst.alfabattle.task3.persistence.Branch
import com.github.m_burst.alfabattle.task3.persistence.BranchDao
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

class BranchNotFoundException : Exception()

@Service
class BranchService(
    private val branchDao: BranchDao
) {
    fun getBranch(id: Int): Branch = transaction {
        branchDao.findById(id) ?: throw BranchNotFoundException()
    }
}
