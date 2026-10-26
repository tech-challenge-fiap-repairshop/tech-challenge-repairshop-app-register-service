package com.cao.repairshop.register.application.usecases.customer

import java.util.UUID

fun interface DeleteCustomer {
    fun execute(id: UUID)
}
