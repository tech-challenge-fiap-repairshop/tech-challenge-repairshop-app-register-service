package com.cao.repairshop.register.domain

import com.cao.repairshop.register.domain.entities.Document
import com.cao.repairshop.core.exception.InvalidDocumentException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DocumentTest {

    @Test
    fun `valid CPF creates document and normalizes value`() {
        val doc = Document("529.982.247-25")
        assertEquals("52998224725", doc.normalized)
    }

    @Test
    fun `valid CNPJ creates document and normalizes value`() {
        val doc = Document("11.222.333/0001-81")
        assertEquals("11222333000181", doc.normalized)
    }

    @Test
    fun `invalid CPF check digits throw InvalidDocumentException`() {
        assertThrows<InvalidDocumentException> {
            Document("529.982.247-99")
        }
    }

    @Test
    fun `all-same-digit CPF throws InvalidDocumentException`() {
        assertThrows<InvalidDocumentException> {
            Document("111.111.111-11")
        }
    }

    @Test
    fun `all-same-digit CNPJ throws InvalidDocumentException`() {
        assertThrows<InvalidDocumentException> {
            Document("11.111.111/1111-11")
        }
    }

    @Test
    fun `invalid length throws InvalidDocumentException`() {
        assertThrows<InvalidDocumentException> {
            Document("12345")
        }
    }

    @Test
    fun `Document of already normalized CPF works`() {
        val doc = Document("52998224725")
        assertEquals("52998224725", doc.normalized)
    }

    @Test
    fun `CPF has 11 digits and type is CPF`() {
        val doc = Document("529.982.247-25")
        assertEquals(Document.DocumentType.CPF, doc.type)
    }

    @Test
    fun `CNPJ has 14 digits and type is CNPJ`() {
        val doc = Document("11.222.333/0001-81")
        assertEquals(Document.DocumentType.CNPJ, doc.type)
    }
}
