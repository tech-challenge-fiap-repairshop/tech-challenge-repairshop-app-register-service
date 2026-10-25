package com.cao.repairshop.inventory.infra.controller.dtos

import com.cao.repairshop.core.validator.annotation.SafeString
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class CreateInsumeRequest(
    @Schema(description = "Part or consumable name", example = "Oil Filter")
    @field:NotBlank @field:SafeString val name: String,

    @Schema(description = "Manufacturer brand", example = "Tecfil")
    @field:SafeString val brand: String? = null,

    @Schema(description = "Stock quantity", example = "15")
    val quantity: Int = 0,

    @Schema(description = "Batch reference price", example = "350.00")
    @field:NotNull val price: BigDecimal,

    @Schema(description = "Unit price per item", example = "35.50")
    @field:NotNull val unityPrice: BigDecimal
)

data class UpdateInsumeRequest(
    @Schema(description = "Part or consumable name", example = "Oil Filter")
    @field:NotBlank @field:SafeString val name: String,

    @Schema(description = "Stock quantity", example = "15")
    val quantity: Int = 0,

    @Schema(description = "Batch reference price", example = "350.00")
    @field:NotNull val price: BigDecimal,

    @Schema(description = "Unit price per item", example = "35.50")
    @field:NotNull val unityPrice: BigDecimal
)

data class InsumeResponse(
    @Schema(description = "Item unique identifier")
    val id: UUID,
    @Schema(description = "Item name", example = "Oil Filter")
    val name: String,
    @Schema(description = "Manufacturer brand", example = "Tecfil")
    val brand: String?,
    @Schema(description = "Inventory SKU code", example = "SKU-OIL-9988")
    val skuId: String,
    @Schema(description = "Current stock quantity", example = "15")
    val quantity: Int,
    @Schema(description = "Batch reference price", example = "350.00")
    val price: BigDecimal,
    @Schema(description = "Unit price per item", example = "35.50")
    val unityPrice: BigDecimal
)


