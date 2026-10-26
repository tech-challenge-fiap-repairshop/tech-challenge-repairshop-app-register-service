package com.cao.repairshop.register.infra.persistence.models.converter

import com.cao.repairshop.register.domain.entities.Plate
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class PlateConverter : AttributeConverter<Plate, String> {

    override fun convertToDatabaseColumn(attribute: Plate?): String? =
        attribute?.normalized

    override fun convertToEntityAttribute(dbData: String?): Plate? =
        dbData?.let { Plate(it) }
}
