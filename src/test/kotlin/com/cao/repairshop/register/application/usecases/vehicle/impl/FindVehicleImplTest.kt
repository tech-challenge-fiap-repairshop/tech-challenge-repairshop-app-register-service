package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.VehicleGateway
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID

class FindVehicleImplTest {

    private lateinit var vehicleGateway: VehicleGateway
    private lateinit var findVehicleImpl: FindVehicleImpl

    @BeforeEach
    fun setup() {
        vehicleGateway = mockk()
        findVehicleImpl = FindVehicleImpl(vehicleGateway)
    }

    @Test
    fun `should find vehicle by id successfully`() {
        val vehicleId = UUID.randomUUID()
        val vehicle = Vehicle(id = vehicleId, customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findById(vehicleId) } returns vehicle

        val result = findVehicleImpl.findById(vehicleId)

        assertEquals(vehicleId, result.id)
        assertEquals("Ford", result.brand)
    }

    @Test
    fun `should throw error when finding by id and not found`() {
        every { vehicleGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            findVehicleImpl.findById(UUID.randomUUID())
        }
    }

    @Test
    fun `should find vehicle by plate successfully`() {
        val plate = Plate("ABC-1234")
        val vehicle = Vehicle(customerId = UUID.randomUUID(), plate = plate, brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findByPlate(plate) } returns vehicle

        val result = findVehicleImpl.verifyAndTakeByPlate(plate)

        assertEquals("Ford", result.brand)
    }

    @Test
    fun `should throw error when finding by plate and not found`() {
        every { vehicleGateway.findByPlate(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            findVehicleImpl.verifyAndTakeByPlate(Plate("ABC-1234"))
        }
    }

    @Test
    fun `should find all vehicles successfully`() {
        val pageable = PageRequest.of(0, 10)
        val vehicle = Vehicle(customerId = UUID.randomUUID(), plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")

        every { vehicleGateway.findAll(pageable) } returns PageImpl(listOf(vehicle))

        val result = findVehicleImpl.findAll(pageable)

        assertEquals(1, result.totalElements)
        assertEquals("Ford", result.content[0].brand)
    }
}
