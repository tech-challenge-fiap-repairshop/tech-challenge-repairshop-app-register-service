package com.cao.repairshop.inventory.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.application.usecases.UpdateInsume
import com.cao.repairshop.inventory.domain.entities.mapper.toResponse
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import com.cao.repairshop.inventory.infra.controller.dtos.UpdateInsumeRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateInsumeImpl(
    private val insumeGateway: InsumeGateway
) : UpdateInsume {
    @Transactional
    override fun execute(id: UUID, request: UpdateInsumeRequest): InsumeResponse {
        val insume = insumeGateway.findById(id) ?: throw EntityNotFoundException(ErrorMessages.Insume.NOT_FOUND)
        
        insume.name = request.name
        insume.quantity = request.quantity
        insume.price = request.price
        insume.unityPrice = request.unityPrice
        
        return insumeGateway.save(insume).toResponse()
    }
}

