package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.register.application.usecases.vehicle.*

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.register.infra.controller.dtos.CreateVehicleRequest
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.application.gateways.VehicleGateway
import com.cao.repairshop.register.application.usecases.customer.FindCustomer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateVehicleImpl(
    private val vehicleGateway: VehicleGateway,
    private val findCustomer: FindCustomer
) : CreateVehicle {

    @Transactional
    override fun execute(request: CreateVehicleRequest): Vehicle {
        val plate = Plate(request.plate)
        val email = Email(request.customerEmail)

        vehicleGateway.findByPlate(plate)?.let {
            throw DuplicateEntityException(ErrorMessages.Vehicle.DUPLICATE_PLATE)
        }

        val customer = findCustomer.findByEmailOrThrow(email)

        val vehicle = Vehicle(
            customerId = customer.id,
            plate = plate,
            brand = request.brand,
            model = request.model,
            color = request.color,
            manufacturingDate = request.manufacturingDate
        )
        return vehicleGateway.save(vehicle)
    }
}
