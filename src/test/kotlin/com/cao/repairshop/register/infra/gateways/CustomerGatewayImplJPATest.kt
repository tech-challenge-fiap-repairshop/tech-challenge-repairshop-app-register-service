package com.cao.repairshop.register.infra.gateways

import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.infra.persistence.models.CustomerEntity
import com.cao.repairshop.register.infra.persistence.repositories.CustomerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.Optional
import java.util.UUID

class CustomerGatewayImplJPATest {

    private lateinit var customerRepository: CustomerRepository
    private lateinit var customerGatewayImplJPA: CustomerGatewayImplJPA

    @BeforeEach
    fun setup() {
        customerRepository = mockk()
        customerGatewayImplJPA = CustomerGatewayImplJPA(customerRepository)
    }

    @Test
    fun `should find by id successfully`() {
        val id = UUID.randomUUID()
        val customerEntity = CustomerEntity(id = id, name = "John", document = Document("12345678909"))
        every { customerRepository.findById(id) } returns Optional.of(customerEntity)

        val result = customerGatewayImplJPA.findById(id)
        assertNotNull(result)
        assertEquals("John", result?.name)
    }

    @Test
    fun `should save customer successfully`() {
        val customer = Customer(id = UUID.randomUUID(), name = "John", document = Document("12345678909"))
        val customerEntity = CustomerEntity(id = customer.id, name = "John", document = Document("12345678909"))
        
        every { customerRepository.save(any()) } returns customerEntity

        val result = customerGatewayImplJPA.save(customer)
        assertEquals("John", result.name)
        verify { customerRepository.save(any()) }
    }

    @Test
    fun `should find all successfully`() {
        val pageable = PageRequest.of(0, 10)
        val customerEntity = CustomerEntity(id = UUID.randomUUID(), name = "John", document = Document("12345678909"))
        every { customerRepository.findAll(pageable) } returns PageImpl(listOf(customerEntity))

        val result = customerGatewayImplJPA.findAll(pageable)
        assertEquals(1, result.totalElements)
    }

    @Test
    fun `should find by email successfully`() {
        val email = Email("john@example.com")
        val customerEntity = CustomerEntity(id = UUID.randomUUID(), name = "John", document = Document("12345678909"), email = email)
        every { customerRepository.findByEmail(email) } returns customerEntity

        val result = customerGatewayImplJPA.findByEmail(email)
        assertNotNull(result)
        assertEquals("John", result?.name)
    }

    @Test
    fun `should delete customer successfully`() {
        val customer = Customer(id = UUID.randomUUID(), name = "John", document = Document("12345678909"))
        every { customerRepository.delete(any()) } returns Unit

        customerGatewayImplJPA.delete(customer)
        verify { customerRepository.delete(any()) }
    }
}
