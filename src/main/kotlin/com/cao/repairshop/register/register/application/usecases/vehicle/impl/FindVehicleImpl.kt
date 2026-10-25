package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.register.application.usecases.vehicle.*

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.application.gateways.VehicleGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FindVehicleImpl(
    private val vehicleGateway: VehicleGateway
) : FindVehicle {

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Vehicle {
        return vehicleGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Vehicle.NOT_FOUND)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Vehicle> {
        return vehicleGateway.findAll(pageable)
    }

    @Transactional(readOnly = true)
    override fun verifyAndTakeByPlate(plate: Plate): Vehicle {
        return vehicleGateway.findByPlate(plate)
            ?: throw EntityNotFoundException(ErrorMessages.Vehicle.NOT_FOUND)
    }
}
