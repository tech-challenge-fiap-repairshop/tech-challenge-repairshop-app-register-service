package com.cao.repairshop.register.application.usecases.vehicle

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface FindVehicle {
    fun findById(id: UUID): Vehicle
    fun findAll(pageable: Pageable): Page<Vehicle>
    fun verifyAndTakeByPlate(plate: Plate): Vehicle
}
