package com.cao.repairshop.user.infra.controller

import com.cao.repairshop.user.infra.controller.interfaces.AuthApi
import com.cao.repairshop.user.application.usecases.AuthenticateUser
import com.cao.repairshop.user.application.usecases.CreateUser
import com.cao.repairshop.user.infra.controller.dtos.CreateUserRequest
import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import com.cao.repairshop.user.infra.controller.dtos.TokenResponse
import com.cao.repairshop.user.infra.controller.dtos.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticateUser: AuthenticateUser,
    private val createUser: CreateUser
) : AuthApi {

    @PostMapping("/login")
    override fun login(@Valid @RequestBody request: LoginRequest): TokenResponse =
        authenticateUser.execute(request)

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    override fun register(@Valid @RequestBody request: CreateUserRequest): UserResponse =
        createUser.execute(request)
}
