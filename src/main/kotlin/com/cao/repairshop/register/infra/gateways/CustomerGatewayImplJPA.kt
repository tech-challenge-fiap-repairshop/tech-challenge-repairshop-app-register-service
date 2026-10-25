package com.cao.repairshop.register.infra.gateways

import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity
import com.cao.repairshop.register.infra.persistence.repositories.CustomerRepository
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.register.domain.entities.mapper.toDomain
import com.cao.repairshop.register.domain.entities.mapper.toEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerGatewayImplJPA(
    private val customerRepository: CustomerRepository
) : CustomerGateway {

    override fun findByDocument(document: Document): Customer? {
        return customerRepository.findByDocument(document)?.toDomain()
    }

    override fun findByEmail(email: Email): Customer? {
        return customerRepository.findByEmail(email)?.toDomain()
    }

    override fun save(customer: Customer): Customer {
        val savedModel = customerRepository.save(customer.toEntity())
        return savedModel.toDomain()
    }

    override fun findById(id: UUID): Customer? {
        return customerRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findAll(pageable: Pageable): Page<Customer> {
        return customerRepository.findAll(pageable).map { it.toDomain() }
    }

    override fun delete(customer: Customer) {
        customerRepository.delete(customer.toEntity())
    }

    

    
}

