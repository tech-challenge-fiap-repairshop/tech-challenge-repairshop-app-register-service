package com.cao.repairshop.user.infra.gateways

import com.cao.repairshop.user.application.gateways.UserGateway
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.infra.persistence.models.UserEntity
import com.cao.repairshop.user.infra.persistence.repositories.UserRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserGatewayImplJPA(
    private val userRepository: UserRepository
) : UserGateway {

    override fun findByEmail(email: String): User? {
        val dataModel = userRepository.findByEmail(email)
        return dataModel?.toEntity()
    }

    override fun findByCpf(cpf: String): User? {
        val cleanCpf = cpf.replace(Regex("\\D"), "")
        val dataModel = userRepository.findByCpf(cleanCpf) ?: userRepository.findByCpf(cpf)
        return dataModel?.toEntity()
    }

    override fun save(user: User): User {
        val dataModel = user.toDataModel()
        val savedDataModel = userRepository.save(dataModel)
        return savedDataModel.toEntity()
    }

    override fun findById(id: UUID): User? {
        val dataModel = userRepository.findById(id).orElse(null)
        return dataModel?.toEntity()
    }

    private fun UserEntity.toEntity(): User {
        return User(
            id = this.id,
            name = this.name,
            function = this.function,
            cpf = this.cpf,
            email = this.email,
            phone = this.phone,
            password = this.password
        )
    }

    private fun User.toDataModel(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            function = this.function,
            cpf = this.cpf,
            email = this.email,
            phone = this.phone,
            password = this.password
        )
    }
}