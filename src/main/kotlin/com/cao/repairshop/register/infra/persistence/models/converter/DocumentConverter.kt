package com.cao.repairshop.register.infra.persistence.models.converter

import com.cao.repairshop.register.domain.entities.Document
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class DocumentConverter : AttributeConverter<Document, String> {

    override fun convertToDatabaseColumn(attribute: Document?): String? =
        attribute?.normalized

    override fun convertToEntityAttribute(dbData: String?): Document? =
        dbData?.let { Document(it) }
}
