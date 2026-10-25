package com.cao.repairshop.inventory.application.usecases.impl

import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.application.usecases.CreateInsume
import com.cao.repairshop.inventory.domain.entities.Insume
import com.cao.repairshop.inventory.domain.entities.mapper.toResponse
import com.cao.repairshop.inventory.infra.controller.dtos.CreateInsumeRequest
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateInsumeImpl(
    private val insumeGateway: InsumeGateway
) : CreateInsume {
    @Transactional
    override fun execute(request: CreateInsumeRequest): InsumeResponse {
        val insume = Insume(
            name = request.name,
            brand = request.brand,
            quantity = request.quantity,
            price = request.price,
            unityPrice = request.unityPrice
        )
        return insumeGateway.save(insume).toResponse()
    }
}

