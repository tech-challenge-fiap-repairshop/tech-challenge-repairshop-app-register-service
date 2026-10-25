package com.cao.repairshop.register.application.gateways

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface VehicleGateway {
    fun findByPlate(plate: Plate): Vehicle?
    fun existsByCustomerId(customerId: UUID): Boolean
    fun save(vehicle: Vehicle): Vehicle
    fun findById(id: UUID): Vehicle?
    fun findAll(pageable: Pageable): Page<Vehicle>
    fun delete(vehicle: Vehicle)
}
