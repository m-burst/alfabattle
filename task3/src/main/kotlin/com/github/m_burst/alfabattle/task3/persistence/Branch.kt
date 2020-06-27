package com.github.m_burst.alfabattle.task3.persistence

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Component

data class Branch(
    val id: Int,
    val title: String,
    val lat: Double,
    val lon: Double,
    val address: String
)

object BranchTable : IntIdTable("branches") {
    val title: Column<String> = text("title")
    val lon: Column<Double> = double("lon")
    val lat: Column<Double> = double("lat")
    val address: Column<String> = text("address")
}

@Component
class BranchDao : IntIdTableDao<Branch, BranchTable>(BranchTable) {

    override fun readRow(r: ResultRow): Branch = with(table) {
        return Branch(
            id = r[id],
            title = r[title],
            lat = r[lat],
            lon = r[lon],
            address = r[address]
        )
    }

    override fun writeStatement(st: UpdateBuilder<*>, entity: Branch, isInsert: Boolean) = with(table) {
        st[title] = entity.title
        st[lat] = entity.lat
        st[lon] = entity.lon
        st[address] = entity.address
    }
}
