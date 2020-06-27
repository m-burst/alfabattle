package com.github.m_burst.alfabattle.task3.dto

import com.github.m_burst.alfabattle.task3.persistence.Branch

data class BranchDto(
    val id: Int,
    val title: String,
    val lon: Double,
    val lat: Double,
    val address: String
) {
    companion object {
        fun of(branch: Branch): BranchDto {
            return BranchDto(
                id = branch.id,
                title = branch.title,
                lon = branch.lon,
                lat = branch.lat,
                address = branch.address
            )
        }
    }
}
