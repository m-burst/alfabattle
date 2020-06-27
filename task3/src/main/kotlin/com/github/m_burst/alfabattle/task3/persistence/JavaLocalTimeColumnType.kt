package com.github.m_burst.alfabattle.task3.persistence

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.Table
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DEFAULT_TIME_STRING_FORMATTER by lazy { DateTimeFormatter.ISO_LOCAL_TIME.withLocale(Locale.ROOT).withZone(ZoneId.systemDefault()) }

class JavaLocalTimeColumnType : ColumnType(), IDateColumnType {
    override fun sqlType(): String = "DATE"

    override fun nonNullValueToString(value: Any): String {
        val instant = when (value) {
            is String -> return value
            is LocalTime -> Instant.from(LocalDate.MIN.atTime(value).atZone(ZoneId.systemDefault()))
            is java.sql.Date -> value.toInstant()
            is java.sql.Timestamp -> value.toInstant()
            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
        }

        return "'${DEFAULT_TIME_STRING_FORMATTER.format(instant)}'"
    }

    override fun valueFromDB(value: Any): Any = when(value) {
        is LocalTime -> value
        is java.sql.Timestamp -> value.toLocalDateTime().toLocalTime()
        is Int -> Instant.ofEpochMilli(value.toLong()).atZone(ZoneId.systemDefault()).toLocalTime()
        is Long -> Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalTime()
        is String -> value
        else -> LocalTime.parse(value.toString())
    }

    override fun notNullValueToDB(value: Any) = when {
        value is LocalTime -> DEFAULT_TIME_STRING_FORMATTER.format(value)
        else -> value
    }

    companion object {
        internal val INSTANCE = JavaLocalTimeColumnType()
    }
}

fun Table.time(name: String): Column<LocalTime> = registerColumn(name, JavaLocalTimeColumnType())
