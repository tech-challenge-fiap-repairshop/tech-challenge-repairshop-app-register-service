package com.cao.repairshop.user.infra.controller.interfaces

import com.cao.repairshop.user.infra.controller.dtos.CreateUserRequest
import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import com.cao.repairshop.user.infra.controller.dtos.TokenResponse
import com.cao.repairshop.user.infra.controller.dtos.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Authentication", description = "User registration and JWT authentication")
interface AuthApi {

    @Operation(summary = "Authenticate and get JWT token")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Authentication successful",
            content = [Content(schema = Schema(implementation = TokenResponse::class))]),
        ApiResponse(responseCode = "401", description = "Invalid credentials", content = [Content()])
    )
    fun login(request: LoginRequest): TokenResponse

    @Operation(summary = "Register a new user")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "User created",
            content = [Content(schema = Schema(implementation = UserResponse::class))]),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Email already exists", content = [Content()])
    )
    fun register(request: CreateUserRequest): UserResponse
}
