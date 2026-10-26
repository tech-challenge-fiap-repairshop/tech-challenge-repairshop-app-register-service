package com.cao.repairshop.user.application.gateways

import com.cao.repairshop.user.domain.entities.User
import java.util.UUID

interface UserGateway {
    fun findByEmail(email: String): User?
    fun findByCpf(cpf: String): User?
    fun save(user: User): User
    fun findById(id: UUID): User?
}