package com.cao.repairshop.register.application.usecases.vehicle

import com.cao.repairshop.register.infra.controller.dtos.UpdateVehicleRequest
import com.cao.repairshop.register.domain.entities.Vehicle
import java.util.UUID

fun interface UpdateVehicle {
    fun execute(id: UUID, request: UpdateVehicleRequest): Vehicle
}
