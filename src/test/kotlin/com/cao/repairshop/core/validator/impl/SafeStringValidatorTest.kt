package com.cao.repairshop.core.validator.impl

import jakarta.validation.ConstraintValidatorContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SafeStringValidatorTest {

    private lateinit var validator: SafeStringValidator
    private val context: ConstraintValidatorContext = mockk()

    @BeforeEach
    fun setUp() {
        validator = SafeStringValidator()
    }

    @ParameterizedTest
    @ValueSource(strings = ["Hello World", "Simple text 123", "User description with spaces and dots.", "Email: test@example.com"])
    fun `isValid should return true for safe strings`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return true for null or blank values`() {
        assertThat(validator.isValid(null, context)).isTrue()
        assertThat(validator.isValid("", context)).isTrue()
        assertThat(validator.isValid("  ", context)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "<script>alert('xss')</script>",
        "<SCRIPT>alert(1)</SCRIPT>",
        "Safe text <script>bad()</script>",
        "<img src='x' onerror='alert(1)'>",
        "javascript:alert(1)",
        "vbscript:msgbox('hi')",
        "<a href='#' onload='run()'>click</a>",
        "eval('console.log(1)')",
        "<div>some html</div>"
    ])
    fun `isValid should return false for dangerous patterns`(value: String) {
        assertThat(validator.isValid(value, context)).isFalse()
    }
}
