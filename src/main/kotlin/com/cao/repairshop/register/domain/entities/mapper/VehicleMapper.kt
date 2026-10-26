package com.cao.repairshop.register.domain.entities.mapper

import com.cao.repairshop.register.infra.controller.dtos.VehicleResponse
import com.cao.repairshop.register.domain.entities.Vehicle

fun Vehicle.toResponse() = VehicleResponse(
    id = id,
    customerId = this.customerId,
    plate = plate.normalized,
    brand = brand,
    model = model,
    color = color,
    manufacturingDate = manufacturingDate,
    lastMaintenance = lastMaintenance,
    created = created,
    updated = updated
)

fun List<Vehicle>.toResponse() = map { it.toResponse() }