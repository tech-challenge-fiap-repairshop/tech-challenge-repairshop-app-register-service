package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.register.application.usecases.customer.*

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.application.gateways.CustomerGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateCustomerImpl(
    private val customerGateway: CustomerGateway
) : UpdateCustomer {

    @Transactional
    override fun execute(id: UUID, request: UpdateCustomerRequest): Customer {
        val customer = customerGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Customer.NOT_FOUND)

        val email = request.email?.let { Email(it) }
        email?.let { e ->
            customerGateway.findByEmail(e)?.let { existingCustomer ->
                if (existingCustomer.id != id) {
                    throw DuplicateEntityException(ErrorMessages.Customer.DUPLICATE_EMAIL)
                }
            }
        }

        customer.updateDetails(
            name = request.name,
            email = email,
            phone = request.phone,
            birthDate = request.birthDate
        )

        return customerGateway.save(customer)
    }
}
