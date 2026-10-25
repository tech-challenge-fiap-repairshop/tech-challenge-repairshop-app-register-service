package com.cao.repairshop.inventory.infra.controller

import com.cao.repairshop.inventory.infra.controller.interfaces.InsumeApi
import com.cao.repairshop.inventory.application.usecases.*
import com.cao.repairshop.inventory.infra.controller.dtos.*
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/insumes")
class InsumeController(
    private val createInsume: CreateInsume,
    private val findInsume: FindInsume,
    private val updateInsume: UpdateInsume,
    private val deleteInsume: DeleteInsume
) : InsumeApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(@Valid @RequestBody request: CreateInsumeRequest): InsumeResponse =
        createInsume.execute(request)

    @GetMapping
    override fun findAll(pageable: Pageable): Page<InsumeResponse> =
        findInsume.findAll(pageable)

    @GetMapping("/{id}")
    override fun findById(@PathVariable id: UUID): InsumeResponse =
        findInsume.findById(id)

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @Valid @RequestBody request: UpdateInsumeRequest): InsumeResponse =
        updateInsume.execute(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: UUID) = deleteInsume.execute(id)
}

