package com.cao.repairshop.register.infra.controller

import com.cao.repairshop.register.infra.controller.interfaces.CustomerApi
import com.cao.repairshop.register.infra.controller.dtos.CreateCustomerRequest
import com.cao.repairshop.register.infra.controller.dtos.CustomerResponse
import com.cao.repairshop.register.infra.controller.dtos.UpdateCustomerRequest
import com.cao.repairshop.register.domain.entities.mapper.toResponse
import com.cao.repairshop.register.application.usecases.customer.CreateCustomer
import com.cao.repairshop.register.application.usecases.customer.DeleteCustomer
import com.cao.repairshop.register.application.usecases.customer.FindCustomer
import com.cao.repairshop.register.application.usecases.customer.UpdateCustomer
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val createCustomer: CreateCustomer,
    private val findCustomer: FindCustomer,
    private val updateCustomer: UpdateCustomer,
    private val deleteCustomer: DeleteCustomer
) : CustomerApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(@Valid @RequestBody request: CreateCustomerRequest): ResponseEntity<CustomerResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(createCustomer.execute(request).toResponse())

    @GetMapping
    override fun findAll(pageable: Pageable): ResponseEntity<Page<CustomerResponse>> =
        ResponseEntity.ok(findCustomer.findAll(pageable).map { it.toResponse() })

    @GetMapping("/{id}")
    override fun findById(@PathVariable id: UUID): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(findCustomer.findById(id).toResponse())

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @Valid @RequestBody request: UpdateCustomerRequest): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(updateCustomer.execute(id, request).toResponse())

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        deleteCustomer.execute(id)
        return ResponseEntity.noContent().build()
    }
}
