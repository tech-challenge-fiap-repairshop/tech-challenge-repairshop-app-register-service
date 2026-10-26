package com.cao.repairshop.register.application.usecases.customer.impl

import com.cao.repairshop.core.exception.BusinessRuleViolationException
import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.register.application.gateways.CustomerGateway
import com.cao.repairshop.register.domain.ServiceOrderExistenceChecker
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.domain.entities.Document
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteCustomerImplTest {

    private lateinit var customerGateway: CustomerGateway
    private lateinit var serviceOrderExistenceChecker: ServiceOrderExistenceChecker
    private lateinit var deleteCustomerImpl: DeleteCustomerImpl

    @BeforeEach
    fun setup() {
        customerGateway = mockk()
        serviceOrderExistenceChecker = mockk()
        deleteCustomerImpl = DeleteCustomerImpl(customerGateway, serviceOrderExistenceChecker)
    }

    @Test
    fun `should delete customer successfully`() {
        val customerId = UUID.randomUUID()
        val customer = Customer(id = customerId, name = "John", document = Document("12345678909"))

        every { customerGateway.findById(customerId) } returns customer
        every { serviceOrderExistenceChecker.existsByCustomerId(customerId) } returns false
        every { customerGateway.delete(customer) } returns Unit

        deleteCustomerImpl.execute(customerId)

        verify { customerGateway.delete(customer) }
    }

    @Test
    fun `should throw error when deleting and customer not found`() {
        every { customerGateway.findById(any()) } returns null

        assertThrows(EntityNotFoundException::class.java) {
            deleteCustomerImpl.execute(UUID.randomUUID())
        }
    }

    @Test
    fun `should throw error when deleting and customer has service orders`() {
        val customerId = UUID.randomUUID()
        val customer = Customer(id = customerId, name = "John", document = Document("12345678909"))

        every { customerGateway.findById(customerId) } returns customer
        every { serviceOrderExistenceChecker.existsByCustomerId(customerId) } returns true

        assertThrows(BusinessRuleViolationException::class.java) {
            deleteCustomerImpl.execute(customerId)
        }
    }
}
