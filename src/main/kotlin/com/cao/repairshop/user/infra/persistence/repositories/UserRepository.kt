package com.cao.repairshop.user.infra.persistence.repositories

import com.cao.repairshop.user.infra.persistence.models.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
    fun findByCpf(cpf: String): UserEntity?
}
