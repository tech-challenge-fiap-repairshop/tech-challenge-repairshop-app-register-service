package com.cao.repairshop.user.application.usecases

import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import com.cao.repairshop.user.infra.controller.dtos.TokenResponse

fun interface AuthenticateUser {
    fun execute(request: LoginRequest): TokenResponse
}
