package com.cao.repairshop.register.application.usecases.vehicle.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.register.application.gateways.VehicleGateway
import com.cao.repairshop.register.application.usecases.customer.FindCustomer
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.infra.controller.dtos.CreateVehicleRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateVehicleImplTest {

    private lateinit var vehicleGateway: VehicleGateway
    private lateinit var findCustomer: FindCustomer
    private lateinit var createVehicleImpl: CreateVehicleImpl

    @BeforeEach
    fun setup() {
        vehicleGateway = mockk()
        findCustomer = mockk()
        createVehicleImpl = CreateVehicleImpl(vehicleGateway, findCustomer)
    }

    @Test
    fun `should create vehicle successfully`() {
        val request = CreateVehicleRequest(
            customerEmail = "test@example.com",
            plate = "ABC-1234",
            brand = "Ford",
            model = "Fiesta"
        )

        val customer = Customer(name = "John", document = Document("12345678909"))

        every { vehicleGateway.findByPlate(any()) } returns null
        every { findCustomer.findByEmailOrThrow(Email(request.customerEmail)) } returns customer
        every { vehicleGateway.save(any()) } answers { firstArg() }

        val response = createVehicleImpl.execute(request)

        assertEquals("Ford", response.brand)
        assertEquals("Fiesta", response.model)
        assertEquals(Plate("ABC-1234").value, response.plate.value)
        assertEquals(customer.id, response.customerId)
        
        verify { vehicleGateway.save(any()) }
    }

    @Test
    fun `should throw error when plate already exists`() {
        val request = CreateVehicleRequest(
            customerEmail = "test@example.com",
            plate = "ABC-1234",
            brand = "Ford",
            model = "Fiesta"
        )

        every { vehicleGateway.findByPlate(any()) } returns mockk<Vehicle>()

        assertThrows(DuplicateEntityException::class.java) {
            createVehicleImpl.execute(request)
        }
    }
}
