package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.core.security.JwtService
import com.cao.repairshop.user.application.gateways.UserGateway
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.domain.entities.UserRole
import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticateUserImplTest {

    private lateinit var userGateway: UserGateway
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtService: JwtService
    private lateinit var authenticateUserImpl: AuthenticateUserImpl

    @BeforeEach
    fun setup() {
        userGateway = mockk()
        passwordEncoder = mockk()
        jwtService = mockk()
        authenticateUserImpl = AuthenticateUserImpl(userGateway, passwordEncoder, jwtService)
    }

    @Test
    fun `should authenticate user successfully`() {
        val request = LoginRequest(
            cpf = "52998224725",
            password = "password"
        )

        val user = User(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = UserRole.ATTENDANT,
            password = "encodedPassword",
            phone = "11999999999"
        )

        every { userGateway.findByCpf(request.cpf) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns true
        every { jwtService.generateToken(user.id, user.function) } returns "generatedToken"

        val response = authenticateUserImpl.execute(request)

        assertEquals("generatedToken", response.token)
        
        verify { userGateway.findByCpf(request.cpf) }
        verify { passwordEncoder.matches(request.password, user.password) }
        verify { jwtService.generateToken(user.id, user.function) }
    }

    @Test
    fun `should throw error when user not found`() {
        val request = LoginRequest(
            cpf = "52998224725",
            password = "password"
        )

        every { userGateway.findByCpf(request.cpf) } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            authenticateUserImpl.execute(request)
        }

        assertEquals(ErrorMessages.User.INVALID_CREDENTIALS, exception.message)
        
        verify { userGateway.findByCpf(request.cpf) }
    }
    
    @Test
    fun `should throw error when password does not match`() {
        val request = LoginRequest(
            cpf = "52998224725",
            password = "wrongpassword"
        )

        val user = User(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = UserRole.ATTENDANT,
            password = "encodedPassword",
            phone = "11999999999"
        )

        every { userGateway.findByCpf(request.cpf) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns false

        val exception = assertThrows(IllegalArgumentException::class.java) {
            authenticateUserImpl.execute(request)
        }

        assertEquals(ErrorMessages.User.INVALID_CREDENTIALS, exception.message)
        
        verify { userGateway.findByCpf(request.cpf) }
        verify { passwordEncoder.matches(request.password, user.password) }
    }
}
