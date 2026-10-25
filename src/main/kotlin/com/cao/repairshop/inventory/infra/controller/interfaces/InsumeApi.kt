package com.cao.repairshop.inventory.infra.controller.interfaces

import com.cao.repairshop.inventory.infra.controller.dtos.CreateInsumeRequest
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import com.cao.repairshop.inventory.infra.controller.dtos.UpdateInsumeRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

@Tag(name = "Inventory", description = "Parts and consumables inventory management")
interface InsumeApi {

    @Operation(summary = "Register a new inventory item")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Item created",
            content = [Content(schema = Schema(implementation = InsumeResponse::class))]),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Duplicate SKU", content = [Content()])
    )
    fun create(request: CreateInsumeRequest): InsumeResponse

    @Operation(summary = "List all inventory items with pagination")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Paginated inventory list"))
    fun findAll(pageable: Pageable): Page<InsumeResponse>

    @Operation(summary = "Find inventory item by ID")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Item found",
            content = [Content(schema = Schema(implementation = InsumeResponse::class))]),
        ApiResponse(responseCode = "404", description = "Item not found", content = [Content()])
    )
    fun findById(id: UUID): InsumeResponse

    @Operation(summary = "Update inventory item")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Item updated",
            content = [Content(schema = Schema(implementation = InsumeResponse::class))]),
        ApiResponse(responseCode = "404", description = "Item not found", content = [Content()])
    )
    fun update(id: UUID, request: UpdateInsumeRequest): InsumeResponse

    @Operation(summary = "Delete inventory item")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Item deleted", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Item not found", content = [Content()])
    )
    fun delete(id: UUID)
}




