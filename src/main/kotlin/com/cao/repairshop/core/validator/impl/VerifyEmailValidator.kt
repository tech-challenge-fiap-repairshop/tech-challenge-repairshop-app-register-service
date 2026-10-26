package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.VerifyEmail
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class VerifyEmailValidator : ConstraintValidator<VerifyEmail, String> {

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true
        return EMAIL_REGEX.matches(value)
    }
}
