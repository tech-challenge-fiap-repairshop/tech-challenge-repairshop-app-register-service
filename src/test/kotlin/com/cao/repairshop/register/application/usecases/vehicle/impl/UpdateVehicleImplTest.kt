package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.VehicleGateway
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.infra.controller.dtos.UpdateVehicleRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdateVehicleImplTest {

    private lateinit var vehicleGateway: VehicleGateway
    private lateinit var updateVehicleImpl: UpdateVehicleImpl

    @BeforeEach
    fun setup() {
        vehicleGateway = mockk()
        updateVehicleImpl = UpdateVehicleImpl(vehicleGateway)
    }

    @Test
    fun `should update vehicle successfully`() {
        val vehicleId = UUID.randomUUID()
        val request = UpdateVehicleRequest(
            plate = "ABC-1234",
            brand = "Ford Updated",
            model = "Fiesta Updated"
        )

        val vehicle = Vehicle(id = vehicleId, customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findById(vehicleId) } returns vehicle
        every { vehicleGateway.findByPlate(Plate("ABC-1234")) } returns null
        every { vehicleGateway.save(any()) } answers { firstArg() }

        val response = updateVehicleImpl.execute(vehicleId, request)

        assertEquals("Ford Updated", response.brand)
        assertEquals("Fiesta Updated", response.model)
        assertEquals(Plate("ABC-1234").value, response.plate.value)
        
        verify { vehicleGateway.save(vehicle) }
    }

    @Test
    fun `should throw error when vehicle not found`() {
        every { vehicleGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            updateVehicleImpl.execute(UUID.randomUUID(), mockk(relaxed = true))
        }
    }

    @Test
    fun `should throw error when plate already exists for another vehicle`() {
        val vehicleId = UUID.randomUUID()
        val request = UpdateVehicleRequest(
            plate = "ABC-1234",
            brand = "Ford Updated",
            model = "Fiesta Updated"
        )

        val vehicle = Vehicle(id = vehicleId, customerId = UUID.randomUUID(), plate = Plate("XYZ-9999"), brand = "Ford", model = "Fiesta")
        val anotherVehicle = Vehicle(id = UUID.randomUUID(), customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Chevy", model = "Cruze")

        every { vehicleGateway.findById(vehicleId) } returns vehicle
        every { vehicleGateway.findByPlate(Plate("ABC-1234")) } returns anotherVehicle

        assertThrows(DuplicateEntityException::class.java) {
            updateVehicleImpl.execute(vehicleId, request)
        }
    }
}
