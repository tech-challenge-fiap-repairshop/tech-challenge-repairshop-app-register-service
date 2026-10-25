package com.cao.repairshop.inventory.infra.persistence.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "tb_insume")
class InsumeEntity(
    @Id
    @Column(name = "id_tb_insume")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 150)
    var name: String,

    @Column(length = 100)
    var brand: String? = null,

    @Column(name = "sku_id", length = 50)
    var skuId: String = "",

    @Column(nullable = false)
    var quantity: Int = 0,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,

    @Column(name = "unity_price", nullable = false, precision = 10, scale = 2)
    var unityPrice: BigDecimal,
) {
    override fun equals(other: Any?): Boolean = other is InsumeEntity && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = "InsumeEntity(id=$id, name=$name)"
}
