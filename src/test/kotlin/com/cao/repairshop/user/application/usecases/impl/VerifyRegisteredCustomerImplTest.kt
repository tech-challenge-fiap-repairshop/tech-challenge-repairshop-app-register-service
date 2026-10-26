package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.user.application.gateways.UserGateway
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.domain.entities.UserRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VerifyRegisteredCustomerImplTest {

    private lateinit var userGateway: UserGateway
    private lateinit var verifyRegisteredCustomerImpl: VerifyRegisteredCustomerImpl

    @BeforeEach
    fun setup() {
        userGateway = mockk()
        verifyRegisteredCustomerImpl = VerifyRegisteredCustomerImpl(userGateway)
    }

    @Test
    fun `should verify and return customer successfully`() {
        val email = "test@example.com"
        val user = User(
            name = "Test User",
            cpf = "52998224725",
            email = email,
            function = UserRole.CUSTOMER,
            password = "encodedPassword",
            phone = "11999999999"
        )

        every { userGateway.findByEmail(email) } returns user

        val result = verifyRegisteredCustomerImpl.execute(email)

        assertEquals(user, result)
        verify { userGateway.findByEmail(email) }
    }

    @Test
    fun `should throw error when customer not found`() {
        val email = "nonexistent@example.com"

        every { userGateway.findByEmail(email) } returns null

        val exception = assertThrows(EntityNotFoundException::class.java) {
            verifyRegisteredCustomerImpl.execute(email)
        }

        assertEquals(ErrorMessages.Customer.USER_NOT_FOUND_FOR_EMAIL, exception.message)
        verify { userGateway.findByEmail(email) }
    }
}
