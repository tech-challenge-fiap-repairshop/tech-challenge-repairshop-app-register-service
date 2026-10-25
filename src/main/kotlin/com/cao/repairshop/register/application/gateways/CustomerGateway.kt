package com.cao.repairshop.register.application.gateways

import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CustomerGateway {
    fun findByDocument(document: Document): Customer?
    fun findByEmail(email: Email): Customer?
    fun save(customer: Customer): Customer
    fun findById(id: UUID): Customer?
    fun findAll(pageable: Pageable): Page<Customer>
    fun delete(customer: Customer)
}
