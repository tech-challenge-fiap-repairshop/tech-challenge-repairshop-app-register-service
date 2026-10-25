package com.cao.repairshop.inventory.infra.gateways

import com.cao.repairshop.inventory.application.gateways.InsumeGateway
import com.cao.repairshop.inventory.domain.entities.Insume
import com.cao.repairshop.inventory.domain.entities.mapper.toDomain
import com.cao.repairshop.inventory.domain.entities.mapper.toEntity
import com.cao.repairshop.inventory.infra.persistence.repositories.InsumeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class InsumeGatewayImplJPA(
    private val insumeRepository: InsumeRepository
) : InsumeGateway {

    override fun save(insume: Insume): Insume {
        val entity = insume.toEntity()
        val saved = insumeRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: UUID): Insume? {
        return insumeRepository.findById(id).getOrNull()?.toDomain()
    }

    override fun findAll(pageable: Pageable): Page<Insume> {
        return insumeRepository.findAll(pageable).map { it.toDomain() }
    }

    override fun delete(insume: Insume) {
        insumeRepository.delete(insume.toEntity())
    }
}
