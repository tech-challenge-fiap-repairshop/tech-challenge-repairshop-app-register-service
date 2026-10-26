package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.core.security.JwtService
import com.cao.repairshop.user.application.usecases.AuthenticateUser
import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import com.cao.repairshop.user.infra.controller.dtos.TokenResponse
import com.cao.repairshop.user.application.gateways.UserGateway
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticateUserImpl(
    private val userGateway: UserGateway,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) : AuthenticateUser {
    override fun execute(request: LoginRequest): TokenResponse {
        val user = userGateway.findByCpf(request.cpf)
            ?.takeIf { passwordEncoder.matches(request.password, it.password) }
            ?: throw IllegalArgumentException(ErrorMessages.User.INVALID_CREDENTIALS)

        val token = jwtService.generateToken(user.id, user.function)
        return TokenResponse(token = token)
    }
}
