package com.cao.repairshop.inventory.domain.entities

import com.cao.repairshop.core.exception.InsufficientStockException
import java.math.BigDecimal
import java.text.Normalizer
import java.util.UUID

class Insume(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var brand: String? = null,
    var skuId: String = "",
    var quantity: Int = 0,
    var price: BigDecimal,
    var unityPrice: BigDecimal
) {

    init {
        if (skuId.isBlank()) generateSku()
    }

    fun generateSku() {
        val brandPart = brand?.let { normalizeSku(it) } ?: "SEM-MARCA"
        val namePart = normalizeSku(name)
        skuId = "SKU-$namePart-$brandPart"
    }

    private fun normalizeSku(text: String): String {
        val normalized = Normalizer.normalize(text.uppercase(), Normalizer.Form.NFD)
        return normalized
            .replace(Regex("[^\\p{ASCII}]"), "")
            .replace(" ", "-")
    }

    fun deductStock(amount: Int) {
        require(amount > 0) { "Deduction amount must be positive, got: $amount" }
        if (quantity < amount)
            throw InsufficientStockException("Insufficient stock for insume $name. Available: $quantity, Required: $amount.")
        quantity -= amount
    }

    fun restoreStock(amount: Int) {
        require(amount > 0) { "Restore amount must be positive, got: $amount" }
        quantity += amount
    }

    override fun equals(other: Any?): Boolean = other is Insume && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = "Insume(id=$id, name=$name)"
}
