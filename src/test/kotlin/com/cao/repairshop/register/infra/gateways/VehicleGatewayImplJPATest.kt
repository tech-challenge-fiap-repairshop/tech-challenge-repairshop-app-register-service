package com.cao.repairshop.register.infra.gateways

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.domain.entities.Vehicle
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity
import com.cao.repairshop.register.infra.persistence.models.VehicleEntity
import com.cao.repairshop.register.infra.persistence.repositories.CustomerRepository
import com.cao.repairshop.register.infra.persistence.repositories.VehicleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class VehicleGatewayImplJPATest {

    private lateinit var vehicleRepository: VehicleRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var vehicleGatewayImplJPA: VehicleGatewayImplJPA

    @BeforeEach
    fun setup() {
        vehicleRepository = mockk()
        customerRepository = mockk()
        vehicleGatewayImplJPA = VehicleGatewayImplJPA(vehicleRepository, customerRepository)
    }

    @Test
    fun `should find by plate successfully`() {
        val plate = Plate("ABC-1234")
        val customerEntity = mockk<CustomerEntity>(relaxed = true)
        val vehicleEntity = VehicleEntity(id = UUID.randomUUID(), brand = "Ford", model = "Fiesta", plate = plate, customer = customerEntity)
        every { vehicleRepository.findByPlate(plate) } returns vehicleEntity

        val result = vehicleGatewayImplJPA.findByPlate(plate)
        assertNotNull(result)
        assertEquals("Ford", result?.brand)
    }

    @Test
    fun `should save vehicle successfully`() {
        val customerId = UUID.randomUUID()
        val vehicle = Vehicle(id = UUID.randomUUID(), customerId = customerId, plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")
        val customerEntity = mockk<CustomerEntity>(relaxed = true)
        every { customerEntity.id } returns customerId
        val vehicleEntity = VehicleEntity(id = vehicle.id, brand = "Ford", model = "Fiesta", plate = Plate("ABC-1234"), customer = customerEntity)
        
        every { customerRepository.getReferenceById(customerId) } returns customerEntity
        every { vehicleRepository.save(any()) } returns vehicleEntity

        val result = vehicleGatewayImplJPA.save(vehicle)
        assertEquals("Ford", result.brand)
        verify { vehicleRepository.save(any()) }
    }

    @Test
    fun `should find by id successfully`() {
        val id = UUID.randomUUID()
        val customerEntity = mockk<CustomerEntity>(relaxed = true)
        val vehicleEntity = VehicleEntity(id = id, brand = "Ford", model = "Fiesta", plate = Plate("ABC-1234"), customer = customerEntity)
        every { vehicleRepository.findById(id) } returns Optional.of(vehicleEntity)

        val result = vehicleGatewayImplJPA.findById(id)
        assertNotNull(result)
        assertEquals("Ford", result?.brand)
    }

    @Test
    fun `should find all successfully`() {
        val pageable = PageRequest.of(0, 10)
        val customerEntity = mockk<CustomerEntity>(relaxed = true)
        val vehicleEntity = VehicleEntity(id = UUID.randomUUID(), brand = "Ford", model = "Fiesta", plate = Plate("ABC-1234"), customer = customerEntity)
        every { vehicleRepository.findAll(pageable) } returns PageImpl(listOf(vehicleEntity))

        val result = vehicleGatewayImplJPA.findAll(pageable)
        assertEquals(1, result.totalElements)
    }

    @Test
    fun `should check if exists by customer id`() {
        val customerId = UUID.randomUUID()
        every { vehicleRepository.existsByCustomerId(customerId) } returns true
        assertTrue(vehicleGatewayImplJPA.existsByCustomerId(customerId))
    }

    @Test
    fun `should delete vehicle successfully`() {
        val customerId = UUID.randomUUID()
        val vehicle = Vehicle(id = UUID.randomUUID(), customerId = customerId, plate = Plate("ABC-1234"), brand = "Ford", model = "Fiesta")
        val customerEntity = mockk<CustomerEntity>(relaxed = true)
        
        every { customerRepository.getReferenceById(customerId) } returns customerEntity
        every { vehicleRepository.delete(any()) } returns Unit

        vehicleGatewayImplJPA.delete(vehicle)
        verify { vehicleRepository.delete(any()) }
    }
}
