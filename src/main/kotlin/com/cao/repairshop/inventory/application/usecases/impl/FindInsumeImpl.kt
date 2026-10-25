package com.cao.repairshop.inventory.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.application.usecases.FindInsume
import com.cao.repairshop.inventory.domain.entities.Insume
import com.cao.repairshop.inventory.domain.entities.mapper.toResponse
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FindInsumeImpl(
    private val insumeGateway: InsumeGateway
) : FindInsume {
    @Transactional(readOnly = true)
    override fun findById(id: UUID): InsumeResponse {
        val insume = insumeGateway.findById(id) ?: throw EntityNotFoundException(ErrorMessages.Insume.NOT_FOUND)
        return insume.toResponse()
    }

    @Transactional(readOnly = true)
    override fun getEntityById(id: UUID): Insume {
        return insumeGateway.findById(id) ?: throw EntityNotFoundException(ErrorMessages.Insume.notFoundById(id))
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<InsumeResponse> {
        return insumeGateway.findAll(pageable).map { it.toResponse() }
    }
}

