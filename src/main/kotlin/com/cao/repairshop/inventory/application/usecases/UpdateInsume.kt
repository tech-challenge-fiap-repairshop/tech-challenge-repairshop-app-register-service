package com.cao.repairshop.inventory.application.usecases

import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse
import com.cao.repairshop.inventory.infra.controller.dtos.UpdateInsumeRequest
import java.util.UUID

fun interface UpdateInsume {
    fun execute(id: UUID, request: UpdateInsumeRequest): InsumeResponse
}

