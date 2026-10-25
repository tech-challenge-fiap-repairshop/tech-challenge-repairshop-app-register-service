package com.cao.repairshop.register.infra.controller.dtos

import com.cao.repairshop.core.validator.annotation.LicensePlate
import com.cao.repairshop.core.validator.annotation.SafeString
import com.cao.repairshop.core.validator.annotation.VerifyEmail
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CreateVehicleRequest(
    @Schema(description = "Contact email address (must match an existing user account)", example = "john.smith@example.com")
    @field:NotNull @VerifyEmail val customerEmail: String,

    @Schema(description = "License plate (Legacy ABC-1234 or Mercosul ABC1D23)", example = "ABC1D23")
    @field:NotBlank @field:LicensePlate val plate: String,

    @Schema(description = "Vehicle brand", example = "Toyota")
    @field:NotBlank @field:SafeString val brand: String,

    @Schema(description = "Vehicle model", example = "Corolla")
    @field:NotBlank @field:SafeString val model: String,

    @Schema(description = "Vehicle color", example = "Silver")
    @field:SafeString val color: String? = null,

    @Schema(description = "Manufacturing date", example = "2022-06-15")
    @field:PastOrPresent(message = "Manufacturing date cannot be in the future.") val manufacturingDate: LocalDate? = null
)

data class UpdateVehicleRequest(
    @Schema(description = "License plate (Legacy ABC-1234 or Mercosul ABC1D23)", example = "ABC1D23")
    @field:NotBlank @field:LicensePlate val plate: String,

    @Schema(description = "Vehicle brand", example = "Toyota")
    @field:NotBlank @field:SafeString val brand: String,

    @Schema(description = "Vehicle model", example = "Corolla")
    @field:NotBlank @field:SafeString val model: String,

    @Schema(description = "Vehicle color", example = "Silver")
    @field:SafeString val color: String? = null,

    @Schema(description = "Manufacturing date", example = "2022-06-15")
    @field:PastOrPresent(message = "Manufacturing date cannot be in the future.")
    val manufacturingDate: LocalDate? = null
)

data class VehicleResponse(
    @Schema(description = "Vehicle unique identifier")
    val id: UUID,
    @Schema(description = "Owner customer ID")
    val customerId: UUID,
    @Schema(description = "License plate", example = "ABC1D23")
    val plate: String,
    @Schema(description = "Brand", example = "Toyota")
    val brand: String,
    @Schema(description = "Model", example = "Corolla")
    val model: String,
    @Schema(description = "Color", example = "Silver")
    val color: String?,
    @Schema(description = "Manufacturing date", example = "2022-06-15")
    val manufacturingDate: LocalDate?,
    @Schema(description = "Last maintenance date")
    val lastMaintenance: LocalDateTime?,
    @Schema(description = "Record creation timestamp")
    val created: LocalDateTime?,
    @Schema(description = "Last update timestamp")
    val updated: LocalDateTime?
)
