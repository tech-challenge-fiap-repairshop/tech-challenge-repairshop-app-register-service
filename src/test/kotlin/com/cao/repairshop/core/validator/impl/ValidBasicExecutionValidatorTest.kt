package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.execution.domain.BasicExecution
import jakarta.validation.ConstraintValidatorContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ValidBasicExecutionValidatorTest {

    private lateinit var validator: ValidBasicExecutionValidator
    private val context: ConstraintValidatorContext = mockk(relaxed = true)
    private val violationBuilder: ConstraintValidatorContext.ConstraintViolationBuilder = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        validator = ValidBasicExecutionValidator()
        every { context.buildConstraintViolationWithTemplate(any()) } returns violationBuilder
    }

    @ParameterizedTest
    @ValueSource(strings = ["OIL_CHANGE", "SUSPENSION_REPLACEMENT", "WHEEL_ALIGNMENT", "BRAKE_INSPECTION", "ENGINE_DIAGNOSIS", "OTHER"])
    fun `isValid should return true for valid enum names`(value: String) {
        assertThat(validator.isValid(value, context)).isTrue()
    }

    @Test
    fun `isValid should return true for lowercase valid enum names`() {
        assertThat(validator.isValid("oil_change", context)).isTrue()
    }

    @Test
    fun `isValid should return true for null or blank values`() {
        assertThat(validator.isValid(null, context)).isTrue()
        assertThat(validator.isValid("", context)).isTrue()
        assertThat(validator.isValid("  ", context)).isTrue()
    }

    @Test
    fun `isValid should return false and build custom message for invalid values`() {
        val invalidValue = "INVALID_CATEGORY"
        
        val result = validator.isValid(invalidValue, context)
        
        assertThat(result).isFalse()
        verify { context.disableDefaultConstraintViolation() }
        verify { 
            context.buildConstraintViolationWithTemplate(
                match { it.contains("Invalid service category") && it.contains("OIL_CHANGE") && it.contains("OTHER") }
            ) 
        }
    }
}
