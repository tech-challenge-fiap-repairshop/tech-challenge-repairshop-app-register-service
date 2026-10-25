package com.cao.repairshop.register.domain.entities.mapper

import com.cao.repairshop.register.infra.controller.dtos.CustomerResponse
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity


fun Customer.toResponse() = CustomerResponse(
    id = id,
    name = name,
    document = document.normalized,
    email = email?.value,
    phone = phone,
    birthDate = birthDate,
    created = created,
    updated = updated
)

fun List<Customer>.toResponse() = map { it.toResponse() }


fun CustomerEntity.toDomain(): Customer {
    return Customer(
        id = this.id,
        name = this.name,
        document = this.document,
        email = this.email,
        phone = this.phone,
        birthDate = this.birthDate,
        created = this.created,
        updated = this.updated
    )
}

fun Customer.toEntity(): CustomerEntity {
    return CustomerEntity(
        id = this.id,
        name = this.name,
        document = this.document,
        email = this.email,
        phone = this.phone,
        birthDate = this.birthDate,
        created = this.created,
        updated = this.updated
    )
}

