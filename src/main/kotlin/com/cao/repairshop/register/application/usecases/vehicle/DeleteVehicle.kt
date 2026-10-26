package com.cao.repairshop.register.application.usecases.vehicle

import java.util.UUID

fun interface DeleteVehicle {
    fun execute(id: UUID)
}
