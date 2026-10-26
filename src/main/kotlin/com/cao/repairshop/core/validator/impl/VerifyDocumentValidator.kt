package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.VerifyDocument
import com.cao.repairshop.register.domain.entities.Document
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class VerifyDocumentValidator : ConstraintValidator<VerifyDocument, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true
        return try {
            Document(value)
            true
        } catch (_: Exception) {
            false
        }
    }
}
