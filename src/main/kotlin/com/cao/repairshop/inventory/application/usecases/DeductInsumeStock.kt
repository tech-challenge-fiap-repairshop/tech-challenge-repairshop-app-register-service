package com.cao.repairshop.inventory.application.usecases

import java.util.UUID

fun interface DeductInsumeStock {
    fun execute(id: UUID, amount: Int)
}
