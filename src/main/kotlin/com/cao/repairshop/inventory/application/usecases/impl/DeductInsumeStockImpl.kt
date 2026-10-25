package com.cao.repairshop.inventory.application.usecases.impl

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.application.usecases.DeductInsumeStock
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class DeductInsumeStockImpl(
    private val insumeGateway: InsumeGateway
) : DeductInsumeStock {
    @Transactional
    override fun execute(id: UUID, amount: Int) {
        val insume = insumeGateway.findById(id) ?: throw EntityNotFoundException(ErrorMessages.Insume.NOT_FOUND)
        logger.info { "Deducting stock: insumeId=$id, amount=$amount, currentQty=${insume.quantity}" }
        insume.deductStock(amount)
        insumeGateway.save(insume)
        logger.info { "Stock deducted: insumeId=$id, newQty=${insume.quantity}" }
    }
}
