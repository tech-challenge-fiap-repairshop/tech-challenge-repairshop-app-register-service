package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.user.application.usecases.VerifyRegisteredCustomer
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.application.gateways.UserGateway
import org.springframework.stereotype.Service

@Service
class VerifyRegisteredCustomerImpl(
    private val userGateway: UserGateway
) : VerifyRegisteredCustomer {
    override fun execute(email: String): User {
        val user = userGateway.findByEmail(email)
            ?: throw EntityNotFoundException(ErrorMessages.Customer.USER_NOT_FOUND_FOR_EMAIL)
        return user
    }
}
