package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.register.application.usecases.customer.*

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.user.application.usecases.VerifyRegisteredCustomer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCustomerImpl(
    private val customerGateway: CustomerGateway,
    private val verifyRegisteredCustomer: VerifyRegisteredCustomer
) : CreateCustomer {

    @Transactional
    override fun execute(request: CreateCustomerRequest): Customer {
        val document = Document(request.document)
        val email = Email(request.email)

        verifyRegisteredCustomer.execute(request.email)

        customerGateway.findByDocument(document)?.let {
            throw DuplicateEntityException(ErrorMessages.Customer.DUPLICATE_DOCUMENT)
        }

        customerGateway.findByEmail(email)?.let {
            throw DuplicateEntityException(ErrorMessages.Customer.DUPLICATE_EMAIL)
        }

        val customer = Customer(
            name = request.name,
            document = document,
            email = email,
            phone = request.phone,
            birthDate = request.birthDate
        )
        return customerGateway.save(customer)
    }
}
