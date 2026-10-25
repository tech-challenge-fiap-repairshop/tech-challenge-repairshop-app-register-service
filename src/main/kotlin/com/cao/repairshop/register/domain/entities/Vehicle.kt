package com.cao.repairshop.register.domain.entities

import java.time.LocalDateTime
import java.util.UUID

class Vehicle(
    val id: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    var color: String? = null,
    var plate: Plate,
    var customerId: UUID,
    var manufacturingDate: java.time.LocalDate? = null,
    var lastMaintenance: LocalDateTime? = null,
    var created: LocalDateTime? = null,
    var updated: LocalDateTime? = null
) {
    fun updateDetails(plate: Plate, brand: String, model: String, color: String?, manufacturingDate: java.time.LocalDate?) {
        this.brand = brand
        this.model = model
        this.color = color
        this.plate = plate
        this.manufacturingDate = manufacturingDate
    }
}
