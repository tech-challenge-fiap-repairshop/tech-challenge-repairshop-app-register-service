package com.cao.repairshop.core.validator.annotation

import com.cao.repairshop.core.validator.impl.LicensePlateValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [LicensePlateValidator::class])
@MustBeDocumented
annotation class LicensePlate(
    val message: String = "Invalid license plate. Accepted: Legacy (ABC-1234) or Mercosul (ABC1D23)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
