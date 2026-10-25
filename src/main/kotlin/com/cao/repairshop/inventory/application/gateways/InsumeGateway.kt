package com.cao.repairshop.inventory.application.gateways

import com.cao.repairshop.inventory.domain.entities.Insume
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface InsumeGateway {
    fun save(insume: Insume): Insume
    fun findById(id: UUID): Insume?
    fun findAll(pageable: Pageable): Page<Insume>
    fun delete(insume: Insume)
}
