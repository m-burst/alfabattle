package com.github.m_burst.alfabattle.task3.controllers

import com.github.m_burst.alfabattle.task3.dto.BranchDto
import com.github.m_burst.alfabattle.task3.services.BranchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/branches")
    fun getBranch(
        @RequestParam("lat") lat: Double,
        @RequestParam("lon") lon: Double
    ): BranchDto {
        val (branch, distance) = branchService.getNearestBranch(lat = lat, lon = lon)
        return BranchDto.of(branch, distance = distance)
    }
}
