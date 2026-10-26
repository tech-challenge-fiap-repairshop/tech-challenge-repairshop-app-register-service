package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.core.exception.BusinessRuleViolationException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.VehicleGateway
import com.cao.repairshop.register.domain.ServiceOrderExistenceChecker
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteVehicleImplTest {

    private lateinit var vehicleGateway: VehicleGateway
    private lateinit var serviceOrderExistenceChecker: ServiceOrderExistenceChecker
    private lateinit var deleteVehicleImpl: DeleteVehicleImpl

    @BeforeEach
    fun setup() {
        vehicleGateway = mockk()
        serviceOrderExistenceChecker = mockk()
        deleteVehicleImpl = DeleteVehicleImpl(vehicleGateway, serviceOrderExistenceChecker)
    }

    @Test
    fun `should delete vehicle successfully`() {
        val vehicleId = UUID.randomUUID()
        val vehicle = Vehicle(id = vehicleId, customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findById(vehicleId) } returns vehicle
        every { serviceOrderExistenceChecker.existsByVehicleId(vehicleId) } returns false
        every { vehicleGateway.delete(vehicle) } returns Unit

        deleteVehicleImpl.execute(vehicleId)

        verify { vehicleGateway.delete(vehicle) }
    }

    @Test
    fun `should throw error when deleting and vehicle not found`() {
        every { vehicleGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            deleteVehicleImpl.execute(UUID.randomUUID())
        }
    }

    @Test
    fun `should throw error when deleting and vehicle has service orders`() {
        val vehicleId = UUID.randomUUID()
        val vehicle = Vehicle(id = vehicleId, customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findById(vehicleId) } returns vehicle
        every { serviceOrderExistenceChecker.existsByVehicleId(vehicleId) } returns true

        assertThrows(BusinessRuleViolationException::class.java) {
            deleteVehicleImpl.execute(vehicleId)
        }
    }
}
