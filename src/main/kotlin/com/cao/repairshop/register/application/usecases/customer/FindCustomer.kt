package com.cao.repairshop.register.application.usecases.customer

import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Email
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface FindCustomer {
    fun findById(id: UUID): Customer
    fun findByEmailOrThrow(email: Email): Customer
    fun findAll(pageable: Pageable): Page<Customer>
}
