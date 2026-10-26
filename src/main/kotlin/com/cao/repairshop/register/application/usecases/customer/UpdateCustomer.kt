package com.cao.repairshop.register.application.usecases.customer

import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import com.cao.repairshop.register.domain.entities.Customer
import java.util.UUID

fun interface UpdateCustomer {
    fun execute(id: UUID, request: UpdateCustomerRequest): Customer
}
