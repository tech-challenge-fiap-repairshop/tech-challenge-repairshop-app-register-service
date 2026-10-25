package com.cao.repairshop.core.validator.impl

import jakarta.validation.ConstraintValidatorContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class VerifyDocumentValidatorTest {

    private lateinit var validator: VerifyDocumentValidator
    private val context: ConstraintValidatorContext = mockk()

    @BeforeEach
    fun setUp() {
        validator = VerifyDocumentValidator()
    }

    @ParameterizedTest
    @ValueSource(strings = ["529.982.247-25", "52998224725", "123.456.789-09", "12345678909"])
    fun `isValid should return true for valid documents`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return true for null or blank values`() {
        assertThat(validator.isValid(null, context)).isTrue()
        assertThat(validator.isValid("", context)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["111.111.111-11", "123", "abc", "123.456.789-00", "00000000000"])
    fun `isValid should return false for invalid documents`(value: String) {
        assertThat(validator.isValid(value, context)).isFalse()
    }
}
