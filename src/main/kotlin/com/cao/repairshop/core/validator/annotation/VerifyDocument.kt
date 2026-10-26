package com.cao.repairshop.core.validator.annotation

import com.cao.repairshop.core.validator.impl.VerifyDocumentValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [VerifyDocumentValidator::class])
@MustBeDocumented
annotation class VerifyDocument(
    val message: String = "Invalid document (CPF or CNPJ)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
