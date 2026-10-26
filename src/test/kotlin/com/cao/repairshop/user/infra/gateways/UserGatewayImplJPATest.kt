package com.cao.repairshop.user.infra.gateways

import com.cao.repairshop.user.domain.entities.User
import com.cao.repairshop.user.domain.entities.UserRole
import com.cao.repairshop.user.infra.persistence.models.UserEntity
import com.cao.repairshop.user.infra.persistence.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class UserGatewayImplJPATest {

    private lateinit var userRepository: UserRepository
    private lateinit var userGatewayImplJPA: UserGatewayImplJPA

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        userGatewayImplJPA = UserGatewayImplJPA(userRepository)
    }

    @Test
    fun `should find by email successfully`() {
        val email = "john@example.com"
        val userEntity = UserEntity(id = UUID.randomUUID(), name = "John", cpf = "52998224725", email = email, password = "password", function = UserRole.CUSTOMER, phone = "123456789")
        every { userRepository.findByEmail(email) } returns userEntity

        val result = userGatewayImplJPA.findByEmail(email)
        assertNotNull(result)
        assertEquals("John", result?.name)
    }

    @Test
    fun `should find by cpf successfully`() {
        val cpf = "52998224725"
        val userEntity = UserEntity(id = UUID.randomUUID(), name = "John", cpf = cpf, email = "john@example.com", password = "password", function = UserRole.CUSTOMER, phone = "123456789")
        every { userRepository.findByCpf(cpf) } returns userEntity

        val result = userGatewayImplJPA.findByCpf(cpf)
        assertNotNull(result)
        assertEquals("John", result?.name)
        assertEquals(cpf, result?.cpf)
    }

    @Test
    fun `should save user successfully`() {
        val user = User(id = UUID.randomUUID(), name = "John", cpf = "52998224725", email = "john@example.com", password = "password", function = UserRole.CUSTOMER, phone = "123456789")
        val userEntity = UserEntity(id = user.id, name = "John", cpf = "52998224725", email = "john@example.com", password = "password", function = UserRole.CUSTOMER, phone = "123456789")
        
        every { userRepository.save(any()) } returns userEntity

        val result = userGatewayImplJPA.save(user)
        assertEquals("John", result.name)
        assertEquals("52998224725", result.cpf)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should find by id successfully`() {
        val id = UUID.randomUUID()
        val userEntity = UserEntity(id = id, name = "John", cpf = "52998224725", email = "john@example.com", password = "password", function = UserRole.CUSTOMER, phone = "123456789")
        every { userRepository.findById(id) } returns Optional.of(userEntity)

        val result = userGatewayImplJPA.findById(id)
        assertNotNull(result)
        assertEquals("John", result?.name)
    }
}
