package com.github.m_burst.alfabattle.task3.controllers

import com.github.m_burst.alfabattle.task3.dto.BranchDto
import com.github.m_burst.alfabattle.task3.services.BranchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class BranchController(
    private val branchService: BranchService
) {

    @GetMapping("/branches/{id}")
    fun getBranch(
        @PathVariable("id") id: Int
    ): BranchDto {
        return BranchDto.of(branchService.getBranch(id))
    }
}
