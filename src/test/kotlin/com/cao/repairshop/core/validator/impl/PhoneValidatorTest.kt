package com.cao.repairshop.core.validator.impl

import jakarta.validation.ConstraintValidatorContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PhoneValidatorTest {

    private lateinit var validator: PhoneValidator
    private val context: ConstraintValidatorContext = mockk()

    @BeforeEach
    fun setUp() {
        validator = PhoneValidator()
    }

    @ParameterizedTest
    @ValueSource(strings = ["11987654321", "(11) 98765-4321", "+55 11 987654321", "1133221100", "5511999999999"])
    fun `isValid should return true for valid phone formats`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return true for null or blank values`() {
        assertThat(validator.isValid(null, context)).isTrue()
        assertThat(validator.isValid("", context)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["123", "123456789", "12345678901234", "abc123456789", "!!@@##$$%%"])
    fun `isValid should return false for invalid phone lengths`(value: String) {
        assertThat(validator.isValid(value, context)).isFalse()
    }
}
