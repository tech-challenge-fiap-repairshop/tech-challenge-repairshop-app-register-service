package com.cao.repairshop.core.validator.impl

import jakarta.validation.ConstraintValidatorContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class VerifyEmailValidatorTest {

    private lateinit var validator: VerifyEmailValidator
    private val context: ConstraintValidatorContext = mockk()

    @BeforeEach
    fun setUp() {
        validator = VerifyEmailValidator()
    }

    @ParameterizedTest
    @ValueSource(strings = ["test@example.com", "user.name@domain.co.uk", "123@abc.com", "a@b.cd"])
    fun `isValid should return true for valid emails`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return true for null or blank values`() {
        assertThat(validator.isValid(null, context)).isTrue()
        assertThat(validator.isValid("", context)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "plainaddress",
        "#@%^%#$@#$@#.com",
        "@example.com",
        "Joe Smith <email@example.com>",
        "email.example.com",
        "email@example@example.com",
        "あいうえお@example.com",
        "email@example.com (Joe Smith)",
        "email@example",
        "email@111.222.333.44444"
    ])
    fun `isValid should return false for invalid emails`(value: String) {
        assertThat(validator.isValid(value, context)).isFalse()
    }
}
