package com.cao.repairshop.user.domain.entities.mapper

import com.cao.repairshop.user.infra.controller.dtos.UserResponse
import com.cao.repairshop.user.domain.entities.User

fun User.toResponse() = UserResponse(
    id = id,
    name = name,
    function = function,
    cpf = cpf,
    email = email,
    phone = phone
)
