package com.cao.repairshop.register.domain.entities

import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.core.exception.InvalidPlateException

data class Plate(val value: String) {

    init {
        val normalized = value.uppercase().replace("-", "")
        if (!OLD_FORMAT.matches(normalized) && !MERCOSUL_FORMAT.matches(normalized))
            throw InvalidPlateException(ErrorMessages.Plate.INVALID_FORMAT)
    }

    val normalized: String get() = value.uppercase().replace("-", "")

    companion object {
        private val OLD_FORMAT = Regex("^[A-Z]{3}[0-9]{4}$")
        private val MERCOSUL_FORMAT = Regex("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")
    }
}
