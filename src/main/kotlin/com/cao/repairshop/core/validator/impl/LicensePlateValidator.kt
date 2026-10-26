package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.LicensePlate
import com.cao.repairshop.register.domain.entities.Plate
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class LicensePlateValidator : ConstraintValidator<LicensePlate, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return false
        return try {
            Plate(value)
            true
        } catch (_: Exception) {
            false
        }
    }
}
