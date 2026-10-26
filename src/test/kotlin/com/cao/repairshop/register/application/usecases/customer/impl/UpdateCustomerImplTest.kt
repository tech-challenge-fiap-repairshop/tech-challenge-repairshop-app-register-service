package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdateCustomerImplTest {

    private lateinit var customerGateway: CustomerGateway
    private lateinit var updateCustomerImpl: UpdateCustomerImpl

    @BeforeEach
    fun setup() {
        customerGateway = mockk()
        updateCustomerImpl = UpdateCustomerImpl(customerGateway)
    }

    @Test
    fun `should update customer successfully`() {
        val customerId = UUID.randomUUID()
        val request = UpdateCustomerRequest(
            name = "John Updated",
            email = "john@example.com",
            phone = "11999999999"
        )

        val customer = Customer(id = customerId, name = "John Doe", document = Document("12345678909"))

        every { customerGateway.findById(customerId) } returns customer
        every { customerGateway.findByEmail(Email("john@example.com")) } returns null
        every { customerGateway.save(any()) } answers { firstArg() }

        val response = updateCustomerImpl.execute(customerId, request)

        assertEquals("John Updated", response.name)
        assertEquals(Email("john@example.com").value, response.email?.value)
        assertEquals("11999999999", response.phone)
        
        verify { customerGateway.save(customer) }
    }

    @Test
    fun `should throw error when customer not found`() {
        every { customerGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            updateCustomerImpl.execute(UUID.randomUUID(), mockk(relaxed = true))
        }
    }

    @Test
    fun `should throw error when email already exists for another customer`() {
        val customerId = UUID.randomUUID()
        val request = UpdateCustomerRequest(
            name = "John Updated",
            email = "john@example.com"
        )

        val customer = Customer(id = customerId, name = "John Doe", document = Document("12345678909"))
        val anotherCustomer = Customer(id = UUID.randomUUID(), name = "Another", document = Document("12345678909"))

        every { customerGateway.findById(customerId) } returns customer
        every { customerGateway.findByEmail(Email("john@example.com")) } returns anotherCustomer

        assertThrows(DuplicateEntityException::class.java) {
            updateCustomerImpl.execute(customerId, request)
        }
    }
}
