package com.cao.repairshop.register.application.usecases.customer

import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.register.domain.entities.Customer

fun interface CreateCustomer {
    fun execute(request: CreateCustomerRequest): Customer
}
