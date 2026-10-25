package com.cao.repairshop.register.infra.persistence.repositories

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.infra.persistence.models.VehicleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VehicleRepository : JpaRepository<VehicleEntity, UUID> {
    fun findByPlate(plate: Plate): VehicleEntity?
    fun existsByCustomerId(customerId: UUID): Boolean
}
