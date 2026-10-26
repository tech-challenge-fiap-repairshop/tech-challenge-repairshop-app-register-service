package com.cao.repairshop.register.domain

import com.cao.repairshop.register.domain.entities.Email
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `valid email creates Email object`() {
        val email = Email("user@example.com")
        assertThat(email.value).isEqualTo("user@example.com")
    }

    @Test
    fun `email with subdomain is valid`() {
        val email = Email("user@mail.example.com")
        assertThat(email.value).isEqualTo("user@mail.example.com")
    }

    @Test
    fun `email without at sign throws exception`() {
        assertThatThrownBy { Email("invalidemail") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `email without domain throws exception`() {
        assertThatThrownBy { Email("user@") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `empty email throws exception`() {
        assertThatThrownBy { Email("") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `blank email throws exception`() {
        assertThatThrownBy { Email("   ") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
