package com.cao.repairshop.register.infra.controller

import com.cao.repairshop.register.infra.controller.interfaces.VehicleApi
import com.cao.repairshop.register.infra.controller.dtos.CreateVehicleRequest
import com.cao.repairshop.register.infra.controller.dtos.UpdateVehicleRequest
import com.cao.repairshop.register.infra.controller.dtos.VehicleResponse
import com.cao.repairshop.register.domain.entities.mapper.toResponse
import com.cao.repairshop.register.application.usecases.vehicle.CreateVehicle
import com.cao.repairshop.register.application.usecases.vehicle.DeleteVehicle
import com.cao.repairshop.register.application.usecases.vehicle.FindVehicle
import com.cao.repairshop.register.application.usecases.vehicle.UpdateVehicle
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/vehicles")
class VehicleController(
    private val createVehicle: CreateVehicle,
    private val findVehicle: FindVehicle,
    private val updateVehicle: UpdateVehicle,
    private val deleteVehicle: DeleteVehicle
) : VehicleApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(@Valid @RequestBody request: CreateVehicleRequest): ResponseEntity<VehicleResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(createVehicle.execute(request).toResponse())
    }

    @GetMapping
    override fun findAll(pageable: Pageable): ResponseEntity<Page<VehicleResponse>> {
        return ResponseEntity.ok(findVehicle.findAll(pageable).map { it.toResponse() })
    }

    @GetMapping("/{id}")
    override fun findById(@PathVariable id: UUID): ResponseEntity<VehicleResponse> {
        return ResponseEntity.ok(findVehicle.findById(id).toResponse())
    }

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @Valid @RequestBody request: UpdateVehicleRequest): ResponseEntity<VehicleResponse> {
        return ResponseEntity.ok(updateVehicle.execute(id, request).toResponse())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        deleteVehicle.execute(id)
        return ResponseEntity.noContent().build()
    }
}
