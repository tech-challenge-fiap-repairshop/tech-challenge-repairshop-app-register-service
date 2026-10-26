package com.cao.repairshop.register.infra.persistence.repositories

import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<CustomerEntity, UUID> {
    fun findByDocument(document: Document): CustomerEntity?
    fun findByEmail(email: Email): CustomerEntity?
}
