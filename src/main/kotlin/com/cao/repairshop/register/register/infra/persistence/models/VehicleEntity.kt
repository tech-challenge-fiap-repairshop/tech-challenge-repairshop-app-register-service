package com.cao.repairshop.register.infra.persistence.models

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.infra.persistence.models.converter.PlateConverter
import com.cao.repairshop.serviceorder.infra.persistence.models.ServiceOrderEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tb_vehicle")
class VehicleEntity(
    @Id
    @Column(name = "id_tb_vehicle")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: CustomerEntity,

    @Convert(converter = PlateConverter::class)
    @Column(name = "plate", nullable = false, unique = true, length = 7)
    var plate: Plate,

    @Column(nullable = false, length = 50)
    var brand: String,

    @Column(nullable = false, length = 80)
    var model: String,

    @Column(length = 30)
    var color: String? = null,

    @Column(name = "manufacturing_date")
    var manufacturingDate: LocalDate? = null,

    @Column(name = "last_maintenance")
    var lastMaintenance: LocalDateTime? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    var serviceOrders: MutableSet<ServiceOrderEntity> = mutableSetOf(),

    @CreationTimestamp
    @Column(nullable = false)
    var created: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updated: LocalDateTime? = null
) {
    fun updateDetails(plate: Plate, brand: String, model: String, color: String?, manufacturingDate: LocalDate?) {
        this.plate = plate
        this.brand = brand
        this.model = model
        this.color = color
        this.manufacturingDate = manufacturingDate
    }

    override fun equals(other: Any?): Boolean = other is VehicleEntity && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = "VehicleEntity(id=$id, plate=${plate.normalized})"
}

