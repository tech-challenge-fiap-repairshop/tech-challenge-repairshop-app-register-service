package com.cao.repairshop.register.infra.gateways

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity
import com.cao.repairshop.register.infra.persistence.models.VehicleEntity
import com.cao.repairshop.register.infra.persistence.repositories.CustomerRepository
import com.cao.repairshop.register.infra.persistence.repositories.VehicleRepository
import com.cao.repairshop.register.application.gateways.VehicleGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class VehicleGatewayImplJPA(
    private val vehicleRepository: VehicleRepository,
    private val customerRepository: CustomerRepository
) : VehicleGateway {

    override fun findByPlate(plate: Plate): Vehicle? {
        return vehicleRepository.findByPlate(plate)?.toEntity()
    }

    override fun existsByCustomerId(customerId: UUID): Boolean {
        return vehicleRepository.existsByCustomerId(customerId)
    }

    override fun save(vehicle: Vehicle): Vehicle {
        val savedModel = vehicleRepository.save(vehicle.toDataModel())
        return savedModel.toEntity()
    }

    override fun findById(id: UUID): Vehicle? {
        return vehicleRepository.findById(id).orElse(null)?.toEntity()
    }

    override fun findAll(pageable: Pageable): Page<Vehicle> {
        return vehicleRepository.findAll(pageable).map { it.toEntity() }
    }

    override fun delete(vehicle: Vehicle) {
        vehicleRepository.delete(vehicle.toDataModel())
    }

    private fun VehicleEntity.toEntity(): Vehicle {
        return Vehicle(
            id = this.id,
            brand = this.brand,
            model = this.model,
            color = this.color,
            plate = this.plate,
            customerId = this.customer.id,
            manufacturingDate = this.manufacturingDate,
            lastMaintenance = this.lastMaintenance,
            created = this.created,
            updated = this.updated
        )
    }

    private fun Vehicle.toDataModel(): VehicleEntity {
        val customerModel = customerRepository.getReferenceById(this.customerId)
        val model = VehicleEntity(
            id = this.id,
            brand = this.brand,
            model = this.model,
            color = this.color,
            manufacturingDate = this.manufacturingDate,
            lastMaintenance = this.lastMaintenance,
            plate = this.plate,
            customer = customerModel,
            created = this.created,
            updated = this.updated
        )
        return model
    }
}
