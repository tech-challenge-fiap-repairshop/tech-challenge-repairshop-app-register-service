package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.register.application.usecases.customer.*

import com.cao.repairshop.core.exception.BusinessRuleViolationException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.domain.ServiceOrderExistenceChecker
import com.cao.repairshop.register.application.gateways.CustomerGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteCustomerImpl(
    private val customerGateway: CustomerGateway,
    private val serviceOrderExistenceChecker: ServiceOrderExistenceChecker
) : DeleteCustomer {

    @Transactional
    override fun execute(id: UUID) {
        val customer = customerGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Customer.NOT_FOUND)

        if (serviceOrderExistenceChecker.existsByCustomerId(customer.id))
            throw BusinessRuleViolationException(ErrorMessages.Customer.HAS_SERVICE_ORDERS) // Keep same error message for now

        customerGateway.delete(customer)
    }
}
