package com.cao.repairshop.core.validator.impl

import jakarta.validation.ConstraintValidatorContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LicensePlateValidatorTest {

    private lateinit var validator: LicensePlateValidator
    private val context: ConstraintValidatorContext = mockk()

    @BeforeEach
    fun setUp() {
        validator = LicensePlateValidator()
    }

    @ParameterizedTest
    @ValueSource(strings = ["ABC-1234", "ABC1234", "BRA2E19", "BRA-2E19"])
    fun `isValid should return true for valid plates`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return false for null or blank values`() {
        assertThat(validator.isValid(null, context)).isFalse()
        assertThat(validator.isValid("", context)).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["AB1234", "ABC123", "ABCD123", "ABC-123", "123-ABCD", "!!!!-!!!"])
    fun `isValid should return false for invalid plates`(value: String) {
        assertThat(validator.isValid(value, context)).isFalse()
    }
}
