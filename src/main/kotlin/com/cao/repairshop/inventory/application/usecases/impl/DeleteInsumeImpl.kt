package com.cao.repairshop.inventory.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.application.usecases.DeleteInsume
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteInsumeImpl(
    private val insumeGateway: InsumeGateway
) : DeleteInsume {
    @Transactional
    override fun execute(id: UUID) {
        val insume = insumeGateway.findById(id) ?: throw EntityNotFoundException(ErrorMessages.Insume.NOT_FOUND)
        insumeGateway.delete(insume)
    }
}
