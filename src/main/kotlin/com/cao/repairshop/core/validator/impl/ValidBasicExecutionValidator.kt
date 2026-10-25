package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.ValidBasicExecution
import com.cao.repairshop.execution.domain.BasicExecution
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidBasicExecutionValidator : ConstraintValidator<ValidBasicExecution, String> {

    private val validValues = BasicExecution.entries.map { it.name }.toSet()

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return true
        
        val isValid = value.uppercase() in validValues
        
        if (!isValid) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(
                "Invalid service category. Valid values are: ${validValues.joinToString(", ")}"
            ).addConstraintViolation()
        }
        
        return isValid
    }
}
