package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.register.application.usecases.customer.*

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.application.gateways.CustomerGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FindCustomerImpl(
    private val customerGateway: CustomerGateway
) : FindCustomer {

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Customer {
        return customerGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Customer.NOT_FOUND)
    }

    @Transactional(readOnly = true)
    override fun findByEmailOrThrow(email: Email): Customer {
        return customerGateway.findByEmail(email)
            ?: throw EntityNotFoundException(ErrorMessages.Customer.NOT_FOUND)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Customer> {
        return customerGateway.findAll(pageable)
    }
}
