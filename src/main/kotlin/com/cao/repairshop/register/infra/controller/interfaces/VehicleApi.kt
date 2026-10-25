package com.cao.repairshop.register.infra.controller.interfaces

import com.cao.repairshop.register.infra.controller.dtos.CreateVehicleRequest
import com.cao.repairshop.register.infra.controller.dtos.UpdateVehicleRequest
import com.cao.repairshop.register.infra.controller.dtos.VehicleResponse
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

@Tag(name = "Vehicles", description = "Vehicle fleet management")
interface VehicleApi {

    @Operation(summary = "Register a new vehicle")
    @ApiResponses(
        ApiResponse(
            responseCode = "201", description = "Vehicle created successfully",
            content = [Content(schema = Schema(implementation = VehicleResponse::class))]
        ),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Customer not found", content = [Content()])
    )
    fun create(request: CreateVehicleRequest): ResponseEntity<VehicleResponse>

    @Operation(summary = "List all vehicles with pagination")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Paginated vehicle list"))
    fun findAll(@PageableDefault(size = 10) pageable: Pageable): ResponseEntity<Page<VehicleResponse>>

    @Operation(summary = "Find vehicle by ID")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Vehicle found",
            content = [Content(schema = Schema(implementation = VehicleResponse::class))]
        ),
        ApiResponse(responseCode = "404", description = "Vehicle not found", content = [Content()])
    )
    fun findById(id: UUID): ResponseEntity<VehicleResponse>

    @Operation(summary = "Update vehicle data")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Vehicle updated",
            content = [Content(schema = Schema(implementation = VehicleResponse::class))]
        ),
        ApiResponse(responseCode = "404", description = "Vehicle not found", content = [Content()])
    )
    fun update(id: UUID, request: UpdateVehicleRequest): ResponseEntity<VehicleResponse>

    @Operation(summary = "Delete a vehicle")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Vehicle deleted", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Vehicle not found", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Vehicle has existing service orders", content = [Content()])
    )
    fun delete(id: UUID): ResponseEntity<Void>
}
