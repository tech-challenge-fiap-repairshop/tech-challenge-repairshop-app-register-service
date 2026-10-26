package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.register.application.usecases.vehicle.*

import com.cao.repairshop.core.exception.BusinessRuleViolationException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.domain.ServiceOrderExistenceChecker
import com.cao.repairshop.register.application.gateways.VehicleGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteVehicleImpl(
    private val vehicleGateway: VehicleGateway,
    private val serviceOrderExistenceChecker: ServiceOrderExistenceChecker
) : DeleteVehicle {

    @Transactional
    override fun execute(id: UUID) {
        val vehicle = vehicleGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Vehicle.NOT_FOUND)

        if (serviceOrderExistenceChecker.existsByVehicleId(vehicle.id))
            throw BusinessRuleViolationException(ErrorMessages.Vehicle.HAS_SERVICE_ORDERS)

        vehicleGateway.delete(vehicle)
    }
}
