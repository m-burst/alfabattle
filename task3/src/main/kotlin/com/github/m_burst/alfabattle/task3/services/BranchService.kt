package com.github.m_burst.alfabattle.task3.services

import com.github.m_burst.alfabattle.task3.persistence.Branch
import com.github.m_burst.alfabattle.task3.persistence.BranchDao
import com.github.m_burst.alfabattle.task3.persistence.QueueLog
import com.github.m_burst.alfabattle.task3.persistence.QueueLogDao
import com.github.m_burst.alfabattle.util.distance
import com.github.m_burst.alfabattle.util.logger
import org.apache.commons.math3.stat.descriptive.rank.Median
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.Duration

class BranchNotFoundException : Exception()

@Service
class BranchService(
    private val branchDao: BranchDao,
    private val queueLogDao: QueueLogDao
) {

    private val log = logger<BranchService>()

    fun getBranch(id: Int): Branch = transaction {
        branchDao.findById(id) ?: throw BranchNotFoundException()
    }

    fun getNearestBranch(lat: Double, lon: Double): Pair<Branch, Double> {
        val allBranches = transaction { branchDao.findAll() }
        val branch = allBranches.minBy { it.distanceTo(locationLat = lat, locationLon = lon) }!!
        val distance = branch.distanceTo(locationLat = lat, locationLon = lon)
        return branch to distance
    }

    fun predictWaitTime(branchId: Int, dayOfWeek: Int, hourOfDay: Int): Double {
        // TODO: this can be optimized to filter in DB but I don't have time now
        val queueLogs = transaction { queueLogDao.findByBranchId(branchId) }

        val matching = queueLogs.filter {
            it.date.dayOfWeek.value == dayOfWeek &&
                it.endTimeOfWait.hour == hourOfDay
        }
        if (matching.isEmpty()) {
            return 0.0
        }
        log.info("Found ${matching.size} queue logs")

        return Median().evaluate(matching.map { it.waitTimeSeconds }.toDoubleArray())
    }

    private fun Branch.distanceTo(locationLat: Double, locationLon: Double): Double {
        return distance(lat1 = locationLat, lon1 = locationLon, lat2 = lat, lon2 = lon)
    }

    private val QueueLog.waitTimeSeconds: Double
        get() = Duration.between(startTimeOfWait, endTimeOfWait).toMillis() / 1000.0
}
