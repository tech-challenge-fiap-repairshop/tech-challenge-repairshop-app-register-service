package com.cao.repairshop.register.infra.controller

import com.cao.repairshop.core.exception.EntityNotFoundException
import com.cao.repairshop.core.exception.GlobalExceptionHandler
import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.domain.entities.Customer
import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import com.cao.repairshop.register.infra.controller.CustomerController
import com.cao.repairshop.register.application.usecases.customer.CreateCustomer
import com.cao.repairshop.register.application.usecases.customer.FindCustomer
import com.cao.repairshop.register.application.usecases.customer.UpdateCustomer
import com.cao.repairshop.register.application.usecases.customer.DeleteCustomer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.config.SpringDataJackson3Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

class CustomerControllerTest {

    private val createCustomer: CreateCustomer = mockk()
    private val findCustomer: FindCustomer = mockk()
    private val updateCustomer: UpdateCustomer = mockk()
    private val deleteCustomer: DeleteCustomer = mockk()
    private lateinit var mockMvc: MockMvc
    private lateinit var mapper: JsonMapper

    private val customerId = UUID.randomUUID()
    private fun sampleCustomer() = Customer(
        id = customerId,
        name = "Test",
        document = Document("52998224725"),
        email = Email("t@test.com")
    )

    @BeforeEach
    fun setUp() {
        mapper = JsonMapper.builder()
            .addModule(SpringDataJackson3Configuration.PageModule(null))
            .build()

        mockMvc = MockMvcBuilders
            .standaloneSetup(CustomerController(createCustomer, findCustomer, updateCustomer, deleteCustomer))
            .setControllerAdvice(GlobalExceptionHandler())
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .setMessageConverters(JacksonJsonHttpMessageConverter(mapper))
            .build()
    }

    @Test
    fun `POST customers should return 201 with valid body`() {
        val request = CreateCustomerRequest(name = "Test", document = "52998224725", email = "t@test.com")
        every { createCustomer.execute(any()) } returns sampleCustomer()

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Test"))
            .andExpect(jsonPath("$.document").value("52998224725"))
    }

    @Test
    fun `POST customers should return 400 with blank name`() {
        val request = mapOf("name" to "", "document" to "52998224725")

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `GET customers should return 200 with paginated result`() {
        every { findCustomer.findAll(any()) } returns PageImpl(listOf(sampleCustomer()))

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].name").value("Test"))
    }

    @Test
    fun `GET customers by id should return 200`() {
        every { findCustomer.findById(customerId) } returns sampleCustomer()

        mockMvc.perform(get("/customers/$customerId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customerId.toString()))
    }

    @Test
    fun `GET customers by id should return 404 when not found`() {
        every { findCustomer.findById(customerId) } throws EntityNotFoundException("Customer not found")

        mockMvc.perform(get("/customers/$customerId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT customers by id should return 200`() {
        val updateRequest = UpdateCustomerRequest(name = "Updated")
        val updatedCustomer = sampleCustomer().apply { name = "Updated" }
        every { updateCustomer.execute(customerId, any()) } returns updatedCustomer

        mockMvc.perform(
            put("/customers/$customerId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated"))
    }

    @Test
    fun `DELETE customers by id should return 204`() {
        every { deleteCustomer.execute(customerId) } returns Unit

        mockMvc.perform(delete("/customers/$customerId"))
            .andExpect(status().isNoContent)
    }
}
