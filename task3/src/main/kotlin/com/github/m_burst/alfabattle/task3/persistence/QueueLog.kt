package com.github.m_burst.alfabattle.task3.persistence

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

class QueueLog(
    val id: Int,
    val date: LocalDate,
    val startTimeOfWait: LocalTime,
    val endTimeOfWait: LocalTime,
    val endTimeOfService: LocalTime,
    val branchId: Int
)

object QueueLogTable : IntIdTable("queue_log") {
    val date: Column<LocalDate> = date("data")
    val startTimeOfWait: Column<LocalTime> = time("start_time_of_wait")
    val endTimeOfWait: Column<LocalTime> = time("end_time_of_wait")
    val endTimeOfService: Column<LocalTime> = time("end_time_of_service")
    val branchId: Column<Int> = reference("branches_id", BranchTable.id)
}

@Component
class QueueLogDao : IntIdTableDao<QueueLog, QueueLogTable>(QueueLogTable) {
    override fun readRow(r: ResultRow): QueueLog = with(table) {
        QueueLog(
            id = r[id],
            date = r[date],
            startTimeOfWait = r[startTimeOfWait],
            endTimeOfWait = r[endTimeOfWait],
            endTimeOfService = r[endTimeOfService],
            branchId = r[branchId]
        )
    }

    override fun writeStatement(st: UpdateBuilder<*>, entity: QueueLog, isInsert: Boolean) = with(table) {
        st[date] = entity.date
        st[startTimeOfWait] = entity.startTimeOfWait
        st[endTimeOfWait] = entity.endTimeOfWait
        st[endTimeOfService] = entity.endTimeOfService
        st[branchId] = entity.branchId
    }

    fun findByBranchId(branchId: Int): List<QueueLog> {
        return table.select { table.branchId eq branchId }.readRows()
    }
}
