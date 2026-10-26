package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.user.application.usecases.VerifyRegisteredCustomer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateCustomerImplTest {

    private lateinit var customerGateway: CustomerGateway
    private lateinit var verifyRegisteredCustomer: VerifyRegisteredCustomer
    private lateinit var createCustomerImpl: CreateCustomerImpl

    @BeforeEach
    fun setup() {
        customerGateway = mockk()
        verifyRegisteredCustomer = mockk()
        createCustomerImpl = CreateCustomerImpl(customerGateway, verifyRegisteredCustomer)
    }

    @Test
    fun `should create customer successfully`() {
        val request = CreateCustomerRequest(
            name = "John Doe",
            document = "12345678909",
            email = "john@example.com",
            phone = "11999999999"
        )

        every { verifyRegisteredCustomer.execute(request.email) } returns mockk()
        every { customerGateway.findByDocument(any()) } returns null
        every { customerGateway.findByEmail(any()) } returns null
        every { customerGateway.save(any()) } answers { firstArg() }

        val response = createCustomerImpl.execute(request)

        assertEquals("John Doe", response.name)
        assertEquals(Document("12345678909").value, response.document.value)
        assertEquals(Email("john@example.com").value, response.email?.value)
        
        verify { customerGateway.save(any()) }
    }

    @Test
    fun `should throw error when document already exists`() {
        val request = CreateCustomerRequest(
            name = "John Doe",
            document = "12345678909",
            email = "john@example.com"
        )

        every { verifyRegisteredCustomer.execute(request.email) } returns mockk()
        every { customerGateway.findByDocument(any()) } returns mockk<Customer>()

        assertThrows(DuplicateEntityException::class.java) {
            createCustomerImpl.execute(request)
        }
    }

    @Test
    fun `should throw error when email already exists`() {
        val request = CreateCustomerRequest(
            name = "John Doe",
            document = "12345678909",
            email = "john@example.com"
        )

        every { verifyRegisteredCustomer.execute(request.email) } returns mockk()
        every { customerGateway.findByDocument(any()) } returns null
        every { customerGateway.findByEmail(any()) } returns mockk<Customer>()

        assertThrows(DuplicateEntityException::class.java) {
            createCustomerImpl.execute(request)
        }
    }
}
