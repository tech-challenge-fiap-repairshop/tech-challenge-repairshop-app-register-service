package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.core.exception.PasswordEncodingException
import com.cao.repairshop.user.application.usecases.CreateUser
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.domain.entities.UserRole
import com.cao.repairshop.user.infra.controller.dtos.CreateUserRequest
import com.cao.repairshop.user.infra.controller.dtos.UserResponse
import com.cao.repairshop.user.application.gateways.UserGateway
import com.cao.repairshop.user.domain.entities.mapper.toResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUserImpl(
    private val userGateway: UserGateway,
    private val passwordEncoder: PasswordEncoder
) : CreateUser {
    override fun execute(request: CreateUserRequest): UserResponse {
        val normalizedCpf = request.cpf.replace(Regex("\\D"), "")

        userGateway.findByCpf(normalizedCpf)?.let {
            throw DuplicateEntityException(ErrorMessages.User.DUPLICATE_CPF)
        }

        userGateway.findByEmail(request.email)?.let {
            throw DuplicateEntityException(ErrorMessages.User.DUPLICATE_EMAIL)
        }

        val user = User(
            name = request.name,
            function = UserRole.valueOf(request.function.uppercase()),
            cpf = normalizedCpf,
            email = request.email,
            phone = request.phone,
            password = passwordEncoder.encode(request.password)
                ?: throw PasswordEncodingException(ErrorMessages.User.PASSWORD_ENCODING_FAILED),
        )

        return userGateway.save(user).toResponse()
    }
}
