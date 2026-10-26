package com.cao.repairshop.register.infra.converter

import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.register.domain.entities.Email
import com.cao.repairshop.register.domain.entities.Plate
import com.cao.repairshop.register.infra.persistence.models.converter.DocumentConverter
import com.cao.repairshop.register.infra.persistence.models.converter.EmailConverter
import com.cao.repairshop.register.infra.persistence.models.converter.PlateConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConverterTest {

    private val documentConverter = DocumentConverter()
    private val emailConverter = EmailConverter()
    private val plateConverter = PlateConverter()

    @Test
    fun `DocumentConverter converts to normalized string`() {
        val doc = Document("529.982.247-25")
        assertThat(documentConverter.convertToDatabaseColumn(doc)).isEqualTo("52998224725")
    }

    @Test
    fun `DocumentConverter converts null to null`() {
        assertThat(documentConverter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun `DocumentConverter converts from string to Document`() {
        val doc = documentConverter.convertToEntityAttribute("52998224725")
        assertThat(doc).isNotNull
        assertThat(doc!!.normalized).isEqualTo("52998224725")
    }

    @Test
    fun `DocumentConverter converts null string to null`() {
        assertThat(documentConverter.convertToEntityAttribute(null)).isNull()
    }

    @Test
    fun `EmailConverter converts to value string`() {
        val email = Email("user@test.com")
        assertThat(emailConverter.convertToDatabaseColumn(email)).isEqualTo("user@test.com")
    }

    @Test
    fun `EmailConverter converts null to null`() {
        assertThat(emailConverter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun `EmailConverter converts from string to Email`() {
        val email = emailConverter.convertToEntityAttribute("user@test.com")
        assertThat(email).isNotNull
        assertThat(email!!.value).isEqualTo("user@test.com")
    }

    @Test
    fun `EmailConverter converts null string to null`() {
        assertThat(emailConverter.convertToEntityAttribute(null)).isNull()
    }

    @Test
    fun `PlateConverter converts to normalized string`() {
        val plate = Plate("ABC-1234")
        assertThat(plateConverter.convertToDatabaseColumn(plate)).isEqualTo("ABC1234")
    }

    @Test
    fun `PlateConverter converts null to null`() {
        assertThat(plateConverter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun `PlateConverter converts old format from string`() {
        val plate = plateConverter.convertToEntityAttribute("ABC1234")
        assertThat(plate).isNotNull
        assertThat(plate!!.normalized).isEqualTo("ABC1234")
    }

    @Test
    fun `PlateConverter converts Mercosul format from string`() {
        val plate = plateConverter.convertToEntityAttribute("ABC1D23")
        assertThat(plate).isNotNull
        assertThat(plate!!.normalized).isEqualTo("ABC1D23")
    }

    @Test
    fun `PlateConverter converts null string to null`() {
        assertThat(plateConverter.convertToEntityAttribute(null)).isNull()
    }
}
