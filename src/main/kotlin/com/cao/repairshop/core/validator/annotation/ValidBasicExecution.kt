package com.cao.repairshop.core.validator.annotation

import com.cao.repairshop.core.validator.impl.ValidBasicExecutionValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidBasicExecutionValidator::class])
@MustBeDocumented
annotation class ValidBasicExecution(
    val message: String = "Invalid service category",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
