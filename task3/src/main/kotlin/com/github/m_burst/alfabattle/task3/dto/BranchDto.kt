package com.github.m_burst.alfabattle.task3.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.m_burst.alfabattle.task3.persistence.Branch
import kotlin.math.roundToInt

data class BranchDto(
    val id: Int,
    val title: String,
    val lon: Double,
    val lat: Double,
    val address: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val distance: Int?
) {
    companion object {
        fun of(branch: Branch, distance: Double? = null): BranchDto {
            return BranchDto(
                id = branch.id,
                title = branch.title,
                lon = branch.lon,
                lat = branch.lat,
                address = branch.address,
                distance = distance?.roundToInt()
            )
        }
    }
}
