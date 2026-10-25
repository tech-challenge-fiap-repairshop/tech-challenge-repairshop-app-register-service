package com.cao.repairshop.register.domain

import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.core.exception.InvalidPlateException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlateTest {

    @Test
    fun `old format plate creates and normalizes to uppercase without dash`() {
        val plate = Plate("ABC-1234")
        assertEquals("ABC1234", plate.normalized)
    }

    @Test
    fun `Mercosul format plate creates and normalizes correctly`() {
        val plate = Plate("ABC1D23")
        assertEquals("ABC1D23", plate.normalized)
    }

    @Test
    fun `lowercase old format plate normalizes to uppercase`() {
        val plate = Plate("abc-1234")
        assertEquals("ABC1234", plate.normalized)
    }

    @Test
    fun `plate with two letter prefix throws InvalidPlateException`() {
        assertThrows<InvalidPlateException> {
            Plate("AB-1234")
        }
    }

    @Test
    fun `plate with four letter prefix throws InvalidPlateException`() {
        assertThrows<InvalidPlateException> {
            Plate("ABCD123")
        }
    }

    @Test
    fun `plate starting with digits throws InvalidPlateException`() {
        assertThrows<InvalidPlateException> {
            Plate("123ABCD")
        }
    }

    @Test
    fun `Plate of lowercase Mercosul format works`() {
        val plate = Plate("abc1d23")
        assertEquals("ABC1D23", plate.normalized)
    }
}
