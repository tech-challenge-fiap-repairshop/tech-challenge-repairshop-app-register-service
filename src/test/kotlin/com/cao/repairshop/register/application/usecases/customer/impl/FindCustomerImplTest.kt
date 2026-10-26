package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID

class FindCustomerImplTest {

    private lateinit var customerGateway: CustomerGateway
    private lateinit var findCustomerImpl: FindCustomerImpl

    @BeforeEach
    fun setup() {
        customerGateway = mockk()
        findCustomerImpl = FindCustomerImpl(customerGateway)
    }

    @Test
    fun `should find customer by id successfully`() {
        val customerId = UUID.randomUUID()
        val customer = Customer(id = customerId, name = "John", document = Document("12345678909"))

        every { customerGateway.findById(customerId) } returns customer

        val result = findCustomerImpl.findById(customerId)

        assertEquals(customerId, result.id)
        assertEquals("John", result.name)
    }

    @Test
    fun `should throw error when finding by id and not found`() {
        every { customerGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            findCustomerImpl.findById(UUID.randomUUID())
        }
    }

    @Test
    fun `should find customer by email successfully`() {
        val email = Email("john@example.com")
        val customer = Customer(name = "John", document = Document("12345678909"), email = email)

        every { customerGateway.findByEmail(email) } returns customer

        val result = findCustomerImpl.findByEmailOrThrow(email)

        assertEquals("John", result.name)
    }

    @Test
    fun `should throw error when finding by email and not found`() {
        every { customerGateway.findByEmail(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            findCustomerImpl.findByEmailOrThrow(Email("john@example.com"))
        }
    }

    @Test
    fun `should find all customers successfully`() {
        val pageable = PageRequest.of(0, 10)
        val customer = Customer(name = "John", document = Document("12345678909"))

        every { customerGateway.findAll(pageable) } returns PageImpl(listOf(customer))

        val result = findCustomerImpl.findAll(pageable)

        assertEquals(1, result.totalElements)
        assertEquals("John", result.content[0].name)
    }
}
