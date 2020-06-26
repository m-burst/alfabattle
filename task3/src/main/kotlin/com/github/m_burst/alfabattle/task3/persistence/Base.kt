package com.github.m_burst.alfabattle.task3.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

abstract class IdTable<T : Any>(name: String) : Table(name) {
    abstract val id: Column<T>

    override val primaryKey by lazy { PrimaryKey(id) }
}

abstract class LongIdTable(name: String) : IdTable<Long>(name) {
    final override val id: Column<Long> = long("id").autoIncrement()
}

abstract class TableDao<E : Any, T : Table>(open val table: T) {

    protected abstract fun readRow(r: ResultRow): E

    fun Iterable<ResultRow>.readRows(): List<E> {
        return map { r -> readRow(r) }
    }

    protected abstract fun writeStatement(st: UpdateBuilder<*>, entity: E, isInsert: Boolean)

    fun create(entity: E): E = table.insert { writeStatement(it, entity, true) }
        .resultedValues?.readRows()?.first() ?: error("Error: insert returned empty list")

    fun batchCreate(entities: List<E>, ignore: Boolean = false): List<E> = table.batchInsert(entities, ignore) {
        writeStatement(this, it, true)
    }.readRows()

    fun update(where: SqlExpressionBuilder.() -> Op<Boolean>, body: T.(UpdateBuilder<*>) -> Unit) {
        val count = table.update(where, null, body)
        if (count != 1) {
            error("Error: updated count was $count")
        }
    }

    fun update(where: SqlExpressionBuilder.() -> Op<Boolean>, entity: E) {
        update(where) { st -> writeStatement(st, entity, false) }
    }

    fun findAll(): List<E> = table.selectAll().readRows()

    fun deleteWhere(op: SqlExpressionBuilder.() -> Op<Boolean>) {
        table.deleteWhere(op = op)
    }
}

abstract class IdTableDao<E : Any, Id : Comparable<Id>, T : IdTable<Id>>(override val table: T) : TableDao<E, T>(table) {
    fun update(entity: E, id: Id): E {
        update({ table.id eq id }, entity)
        return entity
    }

    fun update(id: Id, body: T.(UpdateBuilder<*>) -> Unit) {
        update({ table.id eq id }, body)
    }

    fun findById(id: Id): E? {
        return table.select { table.id.eq(id) }.readRows().firstOrNull()
    }

    fun findByIds(ids: Iterable<Id>): List<E> {
        return table.select { table.id inList ids }.readRows()
    }

    fun delete(id: Id): Unit = deleteWhere { table.id eq id }
}

abstract class LongIdTableDao<E : Any, T : LongIdTable>(override val table: T) : IdTableDao<E, Long, T>(table)
