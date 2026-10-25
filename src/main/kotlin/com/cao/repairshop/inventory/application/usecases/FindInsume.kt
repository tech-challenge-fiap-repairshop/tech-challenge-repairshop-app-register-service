package com.cao.repairshop.inventory.application.usecases

import com.cao.repairshop.inventory.domain.entities.Insume
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface FindInsume {
    fun findById(id: UUID): InsumeResponse
    fun getEntityById(id: UUID): Insume
    fun findAll(pageable: Pageable): Page<InsumeResponse>
}

