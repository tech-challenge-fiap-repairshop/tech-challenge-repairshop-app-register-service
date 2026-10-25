package com.cao.repairshop.core.validator.annotation

import com.cao.repairshop.core.validator.impl.ValidRoleValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidRoleValidator::class])
@MustBeDocumented
annotation class ValidRole(
    val message: String = "Invalid role. Valid values: CUSTOMER, ATTENDANT",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
