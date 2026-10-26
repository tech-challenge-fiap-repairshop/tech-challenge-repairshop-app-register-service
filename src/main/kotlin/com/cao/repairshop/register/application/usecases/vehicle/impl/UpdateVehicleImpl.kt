package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.register.application.usecases.vehicle.*

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.infra.controller.dtos.UpdateVehicleRequest
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.application.gateways.VehicleGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateVehicleImpl(
    private val vehicleGateway: VehicleGateway
) : UpdateVehicle {

    @Transactional
    override fun execute(id: UUID, request: UpdateVehicleRequest): Vehicle {
        val vehicle = vehicleGateway.findById(id)
            ?: throw EntityNotFoundException(ErrorMessages.Vehicle.NOT_FOUND)

        val plate = Plate(request.plate)

        vehicleGateway.findByPlate(plate)?.let { existingVehicle ->
            if (existingVehicle.id != id) {
                throw DuplicateEntityException(ErrorMessages.Vehicle.DUPLICATE_PLATE)
            }
        }
        vehicle.updateDetails(
            plate = plate,
            brand = request.brand,
            model = request.model,
            color = request.color,
            manufacturingDate = request.manufacturingDate
        )

        return vehicleGateway.save(vehicle)
    }
}
