package com.cao.repairshop.register.infra.controller.dtos

import com.cao.repairshop.core.validator.annotation.Phone
import com.cao.repairshop.core.validator.annotation.SafeString
import com.cao.repairshop.core.validator.annotation.VerifyDocument
import com.cao.repairshop.core.validator.annotation.VerifyEmail
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CreateCustomerRequest(
    @Schema(description = "Full name of the customer", example = "John Smith")
    @field:NotBlank @field:SafeString val name: String,

    @Schema(description = "CPF (11 digits) or CNPJ (14 digits)", example = "52998224725")
    @field:NotBlank @field:VerifyDocument val document: String,

    @Schema(description = "Contact email address (must match an existing user account)", example = "john.smith@example.com")
    @field:NotBlank @field:VerifyEmail val email: String,

    @Schema(description = "Phone number in BR format (10-13 digits)", example = "11999887766")
    @field:Phone val phone: String? = null,

    @Schema(description = "Date of birth", example = "1990-05-20")
    val birthDate: LocalDate? = null
)

data class UpdateCustomerRequest(
    @Schema(description = "Full name of the customer", example = "John Smith")
    @field:NotBlank @field:SafeString val name: String,

    @Schema(description = "Contact email address", example = "john.smith@example.com")
    @field:VerifyEmail
    val email: String? = null,

    @Schema(description = "Phone number in BR format (10-13 digits)", example = "11999887766")
    @field:Phone val phone: String? = null,

    @Schema(description = "Date of birth", example = "1990-05-20")
    val birthDate: LocalDate? = null
)

data class CustomerResponse(
    @Schema(description = "Customer unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: UUID,
    @Schema(description = "Full name", example = "John Smith")
    val name: String,
    @Schema(description = "CPF or CNPJ (normalized)", example = "52998224725")
    val document: String,
    @Schema(description = "Contact email", example = "john.smith@example.com")
    val email: String?,
    @Schema(description = "Phone number", example = "11999887766")
    val phone: String?,
    @Schema(description = "Date of birth", example = "1990-05-20")
    val birthDate: LocalDate?,
    @Schema(description = "Record creation timestamp")
    val created: LocalDateTime?,
    @Schema(description = "Last update timestamp")
    val updated: LocalDateTime?
)
