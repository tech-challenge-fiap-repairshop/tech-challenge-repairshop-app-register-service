package com.cao.repairshop.register.domain.entities

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Customer(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var document: Document,
    var email: Email? = null,
    var phone: String? = null,
    var birthDate: LocalDate? = null,
    var created: LocalDateTime? = null,
    var updated: LocalDateTime? = null
) {
    fun updateDetails(name: String, email: Email?, phone: String?, birthDate: LocalDate?) {
        this.name = name
        this.email = email
        this.phone = phone
        this.birthDate = birthDate
    }
}
