package com.github.m_burst.alfabattle.task3.services

import com.github.m_burst.alfabattle.task3.persistence.Branch
import com.github.m_burst.alfabattle.task3.persistence.BranchDao
import com.github.m_burst.alfabattle.util.distance
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

    fun getNearestBranch(lat: Double, lon: Double): Pair<Branch, Double> {
        val allBranches = transaction { branchDao.findAll() }
        val branch = allBranches.minBy { it.distanceTo(locationLat = lat, locationLon = lon) }!!
        val distance = branch.distanceTo(locationLat = lat, locationLon = lon)
        return branch to distance
    }

    private fun Branch.distanceTo(locationLat: Double, locationLon: Double): Double {
        return distance(lat1 = locationLat, lon1 = locationLon, lat2 = lat, lon2 = lon)
    }
}
