package com.cao.repairshop.register.domain.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class VehicleTest {

    @Test
    fun `should create Vehicle and update details`() {
        val plate = Plate("ABC-1234")
        val customerId = UUID.randomUUID()
        val vehicle = Vehicle(
            brand = "Ford",
            model = "Fiesta",
            color = "Black",
            plate = plate,
            customerId = customerId
        )

        assertEquals("Ford", vehicle.brand)
        assertEquals("Fiesta", vehicle.model)
        assertEquals("Black", vehicle.color)
        assertEquals(plate, vehicle.plate)
        assertEquals(customerId, vehicle.customerId)

        val newPlate = Plate("DEF-5678")
        val newManufacturingDate = LocalDate.of(2015, 1, 1)
        vehicle.updateDetails(
            plate = newPlate,
            brand = "Chevrolet",
            model = "Onix",
            color = "White",
            manufacturingDate = newManufacturingDate
        )

        assertEquals("Chevrolet", vehicle.brand)
        assertEquals("Onix", vehicle.model)
        assertEquals("White", vehicle.color)
        assertEquals(newPlate, vehicle.plate)
        assertEquals(newManufacturingDate, vehicle.manufacturingDate)
    }
}
