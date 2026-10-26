package com.cao.repairshop.register.infra.controller.interfaces

import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.register.infra.controller.dtos.CustomerResponse
import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import java.util.UUID

@Tag(name = "Customers", description = "Customer registration and management")
interface CustomerApi {

    @Operation(summary = "Register a new customer")
    @ApiResponses(
        ApiResponse(
            responseCode = "201", description = "Customer created successfully",
            content = [Content(schema = Schema(implementation = CustomerResponse::class))]
        ),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
        ApiResponse(
            responseCode = "409",
            description = "Customer with this document already exists",
            content = [Content()]
        )
    )
    fun create(request: CreateCustomerRequest): ResponseEntity<CustomerResponse>

    @Operation(summary = "List all customers with pagination")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Paginated customer list"))
    fun findAll(@PageableDefault(size = 10) pageable: Pageable): ResponseEntity<Page<CustomerResponse>>

    @Operation(summary = "Find customer by ID")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Customer found",
            content = [Content(schema = Schema(implementation = CustomerResponse::class))]
        ),
        ApiResponse(responseCode = "404", description = "Customer not found", content = [Content()])
    )
    fun findById(id: UUID): ResponseEntity<CustomerResponse>

    @Operation(summary = "Update customer data")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Customer updated",
            content = [Content(schema = Schema(implementation = CustomerResponse::class))]
        ),
        ApiResponse(responseCode = "404", description = "Customer not found", content = [Content()])
    )
    fun update(id: UUID, request: UpdateCustomerRequest): ResponseEntity<CustomerResponse>

    @Operation(summary = "Delete a customer")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Customer deleted", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Customer not found", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Customer has existing service orders", content = [Content()])
    )
    fun delete(id: UUID): ResponseEntity<Void>
}
