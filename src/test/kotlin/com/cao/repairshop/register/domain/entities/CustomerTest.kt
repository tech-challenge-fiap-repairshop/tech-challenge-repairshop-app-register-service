package com.cao.repairshop.register.domain.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CustomerTest {

    @Test
    fun `should create Customer and update details`() {
        val document = Document("12345678909")
        val email = Email("test@example.com")
        val customer = Customer(
            name = "John Doe",
            document = document,
            email = email,
            phone = "11999999999"
        )

        assertEquals("John Doe", customer.name)
        assertEquals(document, customer.document)
        assertEquals(email, customer.email)
        assertEquals("11999999999", customer.phone)

        val newEmail = Email("new@example.com")
        val newBirthDate = LocalDate.of(1990, 1, 1)
        customer.updateDetails(
            name = "Jane Doe",
            email = newEmail,
            phone = "11888888888",
            birthDate = newBirthDate
        )

        assertEquals("Jane Doe", customer.name)
        assertEquals(newEmail, customer.email)
        assertEquals("11888888888", customer.phone)
        assertEquals(newBirthDate, customer.birthDate)
    }
}
