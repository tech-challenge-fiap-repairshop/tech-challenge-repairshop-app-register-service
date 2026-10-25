package com.cao.repairshop.user.application.usecases.impl

import com.cao.repairshop.core.exception.DuplicateEntityException
import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.user.application.gateways.UserGateway
import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.domain.entities.UserRole
import com.cao.repairshop.user.infra.controller.dtos.CreateUserRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class CreateUserImplTest {

    private lateinit var userGateway: UserGateway
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var createUserImpl: CreateUserImpl

    @BeforeEach
    fun setup() {
        userGateway = mockk()
        passwordEncoder = mockk()
        createUserImpl = CreateUserImpl(userGateway, passwordEncoder)
    }

    @Test
    fun `should create user successfully`() {
        val request = CreateUserRequest(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = "ATTENDANT",
            password = "password",
            phone = "11999999999"
        )

        val user = User(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = UserRole.ATTENDANT,
            password = "encodedPassword",
            phone = "11999999999"
        )

        every { userGateway.findByCpf(request.cpf) } returns null
        every { userGateway.findByEmail(request.email) } returns null
        every { passwordEncoder.encode(request.password) } returns "encodedPassword"
        every { userGateway.save(any()) } returns user

        val response = createUserImpl.execute(request)

        assertEquals("Test User", response.name)
        assertEquals("52998224725", response.cpf)
        assertEquals("test@example.com", response.email)
        assertEquals(UserRole.ATTENDANT, response.function)
        
        verify { userGateway.findByCpf(request.cpf) }
        verify { userGateway.findByEmail(request.email) }
        verify { passwordEncoder.encode(request.password) }
        verify { userGateway.save(any()) }
    }

    @Test
    fun `should throw error when email already exists`() {
        val request = CreateUserRequest(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = "ATTENDANT",
            password = "password",
            phone = "11999999999"
        )

        val existingUser = User(
            name = "Existing User",
            cpf = "52998224725",
            email = "test@example.com",
            function = UserRole.ATTENDANT,
            password = "encodedPassword",
            phone = "11888888888"
        )

        every { userGateway.findByCpf(request.cpf) } returns null
        every { userGateway.findByEmail(request.email) } returns existingUser

        val exception = assertThrows(DuplicateEntityException::class.java) {
            createUserImpl.execute(request)
        }

        assertEquals(ErrorMessages.User.DUPLICATE_EMAIL, exception.message)
        
        verify { userGateway.findByCpf(request.cpf) }
        verify { userGateway.findByEmail(request.email) }
    }

    @Test
    fun `should throw error when password encoding fails`() {
        val request = CreateUserRequest(
            name = "Test User",
            cpf = "52998224725",
            email = "test@example.com",
            function = "ATTENDANT",
            password = "password",
            phone = "11999999999"
        )

        every { userGateway.findByCpf(request.cpf) } returns null
        every { userGateway.findByEmail(request.email) } returns null
        every { passwordEncoder.encode(request.password) } returns null

        val exception = assertThrows(com.cao.repairshop.core.exception.PasswordEncodingException::class.java) {
            createUserImpl.execute(request)
        }

        assertEquals(ErrorMessages.User.PASSWORD_ENCODING_FAILED, exception.message)
        
        verify { userGateway.findByCpf(request.cpf) }
        verify { userGateway.findByEmail(request.email) }
        verify { passwordEncoder.encode(request.password) }
    }
}
