package com.cao.repairshop.register.application.usecases.vehicle

import com.cao.repairshop.register.infra.controller.dtos.CreateVehicleRequest
import com.cao.repairshop.register.domain.entities.Vehicle

fun interface CreateVehicle {
    fun execute(request: CreateVehicleRequest): Vehicle
}
