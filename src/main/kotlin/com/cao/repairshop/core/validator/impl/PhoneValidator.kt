package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.Phone
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PhoneValidator : ConstraintValidator<Phone, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true

        val cleanPhone = value.replace(Regex("[^0-9]"), "")
        return cleanPhone.length in 10..13
    }
}
