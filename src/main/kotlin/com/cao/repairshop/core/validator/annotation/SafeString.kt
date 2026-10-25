package com.cao.repairshop.core.validator.annotation

import com.cao.repairshop.core.validator.impl.SafeStringValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SafeStringValidator::class])
@MustBeDocumented
annotation class SafeString(
    val message: String = "Field contains forbidden characters or scripts (XSS violation)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
