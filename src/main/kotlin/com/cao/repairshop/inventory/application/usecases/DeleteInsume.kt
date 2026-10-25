package com.cao.repairshop.inventory.application.usecases

import java.util.UUID

fun interface DeleteInsume {
    fun execute(id: UUID)
}
