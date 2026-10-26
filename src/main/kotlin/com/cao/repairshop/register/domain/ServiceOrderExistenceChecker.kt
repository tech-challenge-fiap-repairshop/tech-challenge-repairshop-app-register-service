package com.cao.repairshop.register.domain

import java.util.UUID

interface ServiceOrderExistenceChecker {
    fun existsByCustomerId(customerId: UUID): Boolean
    fun existsByVehicleId(vehicleId: UUID): Boolean
}
