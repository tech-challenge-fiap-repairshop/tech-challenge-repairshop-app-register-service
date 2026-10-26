package com.cao.repairshop.user.domain.entities

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var function: UserRole,
    var cpf: String,
    var email: String,
    var phone: String? = null,
    var password: String
)
