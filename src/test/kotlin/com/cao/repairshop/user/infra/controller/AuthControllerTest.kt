package com.cao.repairshop.user.infra.controller

import com.cao.repairshop.core.exception.GlobalExceptionHandler
import com.cao.repairshop.user.domain.entities.UserRole
import com.cao.repairshop.user.infra.controller.dtos.CreateUserRequest
import com.cao.repairshop.user.infra.controller.dtos.LoginRequest
import com.cao.repairshop.user.infra.controller.dtos.TokenResponse
import com.cao.repairshop.user.infra.controller.dtos.UserResponse
import com.cao.repairshop.user.infra.controller.AuthController
import com.cao.repairshop.user.application.usecases.AuthenticateUser
import com.cao.repairshop.user.application.usecases.CreateUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

class AuthControllerTest {

    private val authenticateUser: AuthenticateUser = mockk()
    private val createUser: CreateUser = mockk()
    private lateinit var mockMvc: MockMvc
    private lateinit var mapper: JsonMapper

    @BeforeEach
    fun setUp() {
        mapper = JsonMapper.builder().build()

        mockMvc = MockMvcBuilders
            .standaloneSetup(AuthController(authenticateUser, createUser))
            .setControllerAdvice(GlobalExceptionHandler())
            .setMessageConverters(JacksonJsonHttpMessageConverter(mapper))
            .build()
    }

    @Test
    fun `POST auth login should return 200 with valid credentials`() {
        val request = LoginRequest(cpf = "52998224725", password = "secret123")
        every { authenticateUser.execute(any()) } returns TokenResponse(token = "jwt-token-here")

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("jwt-token-here"))
    }

    @Test
    fun `POST auth login should return 400 with blank cpf`() {
        val request = mapOf("cpf" to "", "password" to "secret123")

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST auth register should return 201`() {
        val request = CreateUserRequest(
            name = "John Doe",
            function = "ATTENDANT",
            cpf = "52998224725",
            email = "john@test.com",
            phone = "+55 11 98888-7777",
            password = "pass1234"
        )
        val userResponse = UserResponse(
            id = UUID.randomUUID(),
            name = "John Doe",
            function = UserRole.ATTENDANT,
            cpf = "52998224725",
            email = "john@test.com",
            phone = "+55 11 98888-7777"
        )
        every { createUser.execute(any()) } returns userResponse

        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.cpf").value("52998224725"))
            .andExpect(jsonPath("$.email").value("john@test.com"))
            .andExpect(jsonPath("$.function").value("ATTENDANT"))
    }
}
