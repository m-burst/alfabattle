package com.github.m_burst.alfabattle.task4.dto

import com.github.m_burst.alfabattle.task4.domain.Person

data class PersonDto(
    val docid: String,
    val fio: String,
    val birthday: String,
    val salary: Double,
    val gender: String
) {
    companion object {
        fun of(person: Person): PersonDto {
            return PersonDto(
                docid = person.docid,
                fio = person.fio,
                birthday = person.birthday,
                salary = person.salary,
                gender = person.gender
            )
        }
    }
}
